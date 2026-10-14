package com.nelinor.autoprop

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import kotlin.math.max
import kotlin.math.min

/**
 * A touch-enabled view that lets the user pan and zoom a bitmap,
 * then crop the currently visible area.
 *
 * Usage:
 *   cropView.setBitmap(bitmap)
 *   val cropped = cropView.getCroppedBitmap()
 */
class CropView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private var bitmap: Bitmap? = null

    // Current transform: translation + scale applied to the bitmap
    private var transX = 0f
    private var transY = 0f
    private var scaleFactor = 1f

    // Paint for drawing the bitmap
    private val bitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)

    // Paint for the crop overlay border
    private val borderPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }

    // Paint for the semi-transparent overlay outside crop area
    private val overlayPaint = Paint().apply {
        color = Color.parseColor("#88000000")
        style = Paint.Style.FILL
    }

    // Grid line paint (rule of thirds)
    private val gridPaint = Paint().apply {
        color = Color.parseColor("#66FFFFFF")
        style = Paint.Style.STROKE
        strokeWidth = 1f
    }

    // Crop rect in view coordinates (the visible crop window)
    private val cropRect = RectF()

    // Gesture detectors
    private val scaleDetector = ScaleGestureDetector(context,
        object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                val factor = detector.scaleFactor
                val newScale = (scaleFactor * factor).coerceIn(0.5f, 8f)
                // Scale around the focal point
                val focusX = detector.focusX
                val focusY = detector.focusY
                transX = focusX - (focusX - transX) * (newScale / scaleFactor)
                transY = focusY - (focusY - transY) * (newScale / scaleFactor)
                scaleFactor = newScale
                clampTranslation()
                invalidate()
                return true
            }
        })

    private val gestureDetector = GestureDetector(context,
        object : GestureDetector.SimpleOnGestureListener() {
            override fun onScroll(
                e1: MotionEvent?,
                e2: MotionEvent,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                transX -= distanceX
                transY -= distanceY
                clampTranslation()
                invalidate()
                return true
            }

            override fun onDoubleTap(e: MotionEvent): Boolean {
                // Double tap: reset to fit
                fitBitmapToView()
                invalidate()
                return true
            }
        })

    fun setBitmap(bmp: Bitmap) {
        bitmap = bmp
        fitBitmapToView()
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // Crop window: full width, BMW iDrive 8 content area ratio (1920×580 ≈ 3.31:1)
        // Nav bar ~140px subtracted from 720px total height
        val cropW = w.toFloat()
        val cropH = cropW / 3.31f
        val cropTop = (h - cropH) / 2f
        cropRect.set(0f, cropTop, cropW, cropTop + cropH)
        fitBitmapToView()
    }

    private fun fitBitmapToView() {
        val bmp = bitmap ?: return
        if (width == 0 || height == 0) return
        // Scale bitmap to fill the crop rect
        val scaleX = cropRect.width() / bmp.width
        val scaleY = cropRect.height() / bmp.height
        scaleFactor = max(scaleX, scaleY)
        // Center bitmap on crop rect
        transX = cropRect.centerX() - bmp.width * scaleFactor / 2f
        transY = cropRect.centerY() - bmp.height * scaleFactor / 2f
    }

    private fun clampTranslation() {
        val bmp = bitmap ?: return
        val bmpW = bmp.width * scaleFactor
        val bmpH = bmp.height * scaleFactor

        // Ensure bitmap always covers the crop rect
        val minX = cropRect.right - bmpW
        val maxX = cropRect.left
        val minY = cropRect.bottom - bmpH
        val maxY = cropRect.top

        transX = if (bmpW >= cropRect.width()) transX.coerceIn(minX, maxX) else transX
        transY = if (bmpH >= cropRect.height()) transY.coerceIn(minY, maxY) else transY
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val bmp = bitmap ?: return

        // Draw bitmap with current transform
        val matrix = Matrix().apply {
            setScale(scaleFactor, scaleFactor)
            postTranslate(transX, transY)
        }
        canvas.drawBitmap(bmp, matrix, bitmapPaint)

        // Draw dark overlay outside crop rect
        val w = width.toFloat()
        val h = height.toFloat()
        // Top
        canvas.drawRect(0f, 0f, w, cropRect.top, overlayPaint)
        // Bottom
        canvas.drawRect(0f, cropRect.bottom, w, h, overlayPaint)
        // Left
        canvas.drawRect(0f, cropRect.top, cropRect.left, cropRect.bottom, overlayPaint)
        // Right
        canvas.drawRect(cropRect.right, cropRect.top, w, cropRect.bottom, overlayPaint)

        // Draw rule-of-thirds grid
        val thirdW = cropRect.width() / 3f
        val thirdH = cropRect.height() / 3f
        for (i in 1..2) {
            canvas.drawLine(cropRect.left + thirdW * i, cropRect.top,
                cropRect.left + thirdW * i, cropRect.bottom, gridPaint)
            canvas.drawLine(cropRect.left, cropRect.top + thirdH * i,
                cropRect.right, cropRect.top + thirdH * i, gridPaint)
        }

        // Draw crop border
        canvas.drawRect(cropRect, borderPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleDetector.onTouchEvent(event)
        gestureDetector.onTouchEvent(event)
        return true
    }

    /**
     * Returns the bitmap cropped to the currently visible crop window.
     * The result is always 1000×302 (3.31:1, BMW iDrive 8 content area 1920×580) for the car screen.
     */
    fun getCroppedBitmap(): Bitmap {
        val bmp = bitmap ?: return Bitmap.createBitmap(1000, 302, Bitmap.Config.ARGB_8888)

        // Map crop rect back to bitmap coordinates
        val invScale = 1f / scaleFactor
        val srcLeft = (cropRect.left - transX) * invScale
        val srcTop = (cropRect.top - transY) * invScale
        val srcRight = (cropRect.right - transX) * invScale
        val srcBottom = (cropRect.bottom - transY) * invScale

        // Clamp to bitmap bounds
        val clampedLeft = max(0f, srcLeft).toInt()
        val clampedTop = max(0f, srcTop).toInt()
        val clampedRight = min(bmp.width.toFloat(), srcRight).toInt()
        val clampedBottom = min(bmp.height.toFloat(), srcBottom).toInt()

        val srcW = clampedRight - clampedLeft
        val srcH = clampedBottom - clampedTop
        if (srcW <= 0 || srcH <= 0) return Bitmap.createBitmap(1000, 302, Bitmap.Config.ARGB_8888)

        // Crop from source bitmap
        val cropped = Bitmap.createBitmap(bmp, clampedLeft, clampedTop, srcW, srcH)

        // Scale to 1000×302 for car screen (BMW iDrive 8 content area, 1920×580, 3.31:1)
        val result = Bitmap.createScaledBitmap(cropped, 1000, 302, true)
        if (cropped !== result) cropped.recycle()
        return result
    }
}
