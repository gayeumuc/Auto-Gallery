package com.nelinor.autoprop.auto

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.Action
import androidx.car.app.model.CarIcon
import androidx.car.app.model.MessageTemplate
import androidx.car.app.model.Pane
import androidx.car.app.model.PaneTemplate
import androidx.car.app.model.Row
import androidx.car.app.model.Template
import androidx.core.graphics.drawable.IconCompat
import androidx.lifecycle.lifecycleScope
import com.nelinor.autoprop.InMemoryBitmapStore
import com.nelinor.autoprop.SharedImageState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * Car screen that displays the selected image using PaneTemplate + CarIcon.
 *
 * Category is now POI (not NAVIGATION), so Surface API is not available.
 * Instead we encode the bitmap as a CarIcon and display it via PaneTemplate,
 * which is the largest image display available in Car App Library 1.4.0
 * without Surface access.
 *
 * Max recommended bitmap size for CarIcon: 800×600 px.
 */
class ImageDetailScreen(carContext: CarContext) : Screen(carContext) {

    companion object {
        private const val TAG = "AutoGallery"
        // BMW iDrive 8 DHU: 1920×720, header ~160px + nav bar ~190px → content ~1920×370
        // Car App Library CarIcon bitmap limit is ~1000px on longest side.
        // We use 1000×193 to match 1920:370 ≈ 5.19:1 and fill the content area.
        private const val TARGET_WIDTH = 1000
        private const val TARGET_HEIGHT = 193
    }

    private var currentBitmap: Bitmap? = null
    // Start with isLoading=false so placeholder is shown immediately on launch
    private var isLoading = false
    private var errorMessage: String? = null

    init {
        // Collect imageEvent SharedFlow (replay=1) — fires on EVERY send, even same URL repeated.
        lifecycleScope.launch {
            SharedImageState.imageEvent.collect { event ->
                Log.d(TAG, "imageEvent: ${event.uri} isRemote=${event.isRemote}")
                // Validate URI before loading
                val uriStr = event.uri.toString()
                if (uriStr.isBlank() || uriStr == "null") {
                    currentBitmap = null
                    isLoading = false
                    errorMessage = "No image selected."
                    invalidate()
                } else {
                    loadBitmap(event.uri, event.isRemote)
                }
            }
        }
    }

    private fun loadBitmap(uri: Uri, isRemote: Boolean) {
        Log.d(TAG, "loadBitmap: $uri isRemote=$isRemote")
        isLoading = true
        errorMessage = null
        invalidate()

        lifecycleScope.launch {
            try {
                val bmp = withContext(Dispatchers.IO) {
                    when {
                        uri.scheme == "inmemory" -> {
                            InMemoryBitmapStore.croppedBitmap
                                ?: throw IllegalStateException("No in-memory bitmap available")
                        }
                        isRemote -> downloadBitmap(uri.toString(), TARGET_WIDTH, TARGET_HEIGHT)
                        else -> decodeSampledBitmapFromContent(uri, TARGET_WIDTH, TARGET_HEIGHT)
                    }
                }
                currentBitmap?.recycle()
                currentBitmap = bmp
                isLoading = false
                errorMessage = null
                Log.d(TAG, "loadBitmap success: ${bmp.width}x${bmp.height}")
            } catch (e: Exception) {
                currentBitmap = null
                isLoading = false
                errorMessage = "Could not load image: ${e.message}"
                Log.e(TAG, "loadBitmap error: ${e.message}", e)
            }
            invalidate()
        }
    }

    /** Load the bundled nature image from assets as a fallback bitmap. */
    private fun loadDefaultBitmap(): Bitmap? {
        return try {
            carContext.assets.open("default_nature.jpg").use { stream ->
                val raw = BitmapFactory.decodeStream(stream)
                scaleBitmap(raw, TARGET_WIDTH, TARGET_HEIGHT)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Could not load default nature image: ${e.message}")
            null
        }
    }

    override fun onGetTemplate(): Template {
        if (isLoading) {
            return MessageTemplate.Builder("Loading image…")
                .setTitle("Auto Prop")
                .setHeaderAction(Action.BACK)
                .build()
        }

        val bitmap = currentBitmap
        if (errorMessage != null || bitmap == null) {
            // Try to show the bundled nature image as default
            val defaultBmp = loadDefaultBitmap()
            if (defaultBmp != null) {
                val defaultIcon = CarIcon.Builder(
                    IconCompat.createWithBitmap(defaultBmp)
                ).build()

                val defaultRow = Row.Builder()
                    .setTitle(if (errorMessage != null) "Could not load image" else "No image selected")
                    .setImage(defaultIcon, Row.IMAGE_TYPE_LARGE)
                    .build()

                val defaultPane = Pane.Builder()
                    .addRow(defaultRow)
                    .addAction(
                        Action.Builder()
                            .setTitle("Back")
                            .setOnClickListener { screenManager.pop() }
                            .build()
                    )
                    .build()

                return PaneTemplate.Builder(defaultPane)
                    .setTitle("Auto Prop")
                    .setHeaderAction(Action.BACK)
                    .build()
            }

            // Fallback to icon if assets image also fails
            val placeholderIcon = CarIcon.Builder(
                IconCompat.createWithResource(carContext, com.nelinor.autoprop.R.drawable.ic_image_placeholder)
            ).build()

            val errorRow = Row.Builder()
                .setTitle(errorMessage ?: "No image selected.")
                .setImage(placeholderIcon, Row.IMAGE_TYPE_LARGE)
                .build()

            val errorPane = Pane.Builder()
                .addRow(errorRow)
                .addAction(
                    Action.Builder()
                        .setTitle("Back")
                        .setOnClickListener { screenManager.pop() }
                        .build()
                )
                .build()

            return PaneTemplate.Builder(errorPane)
                .setTitle("Auto Prop")
                .setHeaderAction(Action.BACK)
                .build()
        }

        // Encode bitmap as CarIcon and display via PaneTemplate
        val icon = CarIcon.Builder(
            IconCompat.createWithBitmap(bitmap)
        ).build()

        val imageName = SharedImageState.selectedImageName.value.ifBlank { "Photo" }

        val row = Row.Builder()
            .setTitle(imageName)
            .setImage(icon, Row.IMAGE_TYPE_LARGE)
            .build()

        val pane = Pane.Builder()
            .addRow(row)
            .addAction(
                Action.Builder()
                    .setTitle("Back")
                    .setOnClickListener { screenManager.pop() }
                    .build()
            )
            .build()

        return PaneTemplate.Builder(pane)
            .setTitle("Auto Prop")
            .setHeaderAction(Action.BACK)
            .build()
    }

    // ── Bitmap loading helpers ────────────────────────────────────────────────

    private fun downloadBitmap(url: String, maxW: Int, maxH: Int): Bitmap {
        val conn = URL(url).openConnection() as HttpURLConnection
        conn.connectTimeout = 10_000
        conn.readTimeout = 15_000
        conn.instanceFollowRedirects = true
        conn.connect()
        val stream: InputStream = conn.inputStream
        val raw = BitmapFactory.decodeStream(stream)
        stream.close()
        conn.disconnect()
        return scaleBitmap(raw, maxW, maxH)
    }

    private fun decodeSampledBitmapFromContent(uri: Uri, maxW: Int, maxH: Int): Bitmap {
        val opts = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        carContext.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it, null, opts)
        }
        opts.inSampleSize = calculateInSampleSize(opts, maxW, maxH)
        opts.inJustDecodeBounds = false
        val bmp = carContext.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it, null, opts)
        } ?: throw IllegalStateException("Cannot open content URI: $uri")
        return scaleBitmap(bmp, maxW, maxH)
    }

    /**
     * Scale and center-crop the bitmap to exactly [maxW]×[maxH] so it fills
     * the Tesla central display (16:10) without letterboxing.
     */
    private fun scaleBitmap(src: Bitmap, maxW: Int, maxH: Int): Bitmap {
        val srcW = src.width
        val srcH = src.height

        // Scale so the image COVERS the target (fill, not fit)
        val scale = maxOf(maxW.toFloat() / srcW, maxH.toFloat() / srcH)
        val scaledW = (srcW * scale).toInt()
        val scaledH = (srcH * scale).toInt()

        val scaled = Bitmap.createScaledBitmap(src, scaledW, scaledH, true)
        if (scaled !== src) src.recycle()

        // Center-crop to exact target size
        val cropX = ((scaledW - maxW) / 2).coerceAtLeast(0)
        val cropY = ((scaledH - maxH) / 2).coerceAtLeast(0)
        val cropW = minOf(maxW, scaledW)
        val cropH = minOf(maxH, scaledH)

        val cropped = Bitmap.createBitmap(scaled, cropX, cropY, cropW, cropH)
        if (cropped !== scaled) scaled.recycle()
        return cropped
    }

    private fun calculateInSampleSize(opts: BitmapFactory.Options, reqW: Int, reqH: Int): Int {
        val (h, w) = opts.outHeight to opts.outWidth
        var inSampleSize = 1
        if (h > reqH || w > reqW) {
            val halfH = h / 2
            val halfW = w / 2
            while (halfH / inSampleSize >= reqH && halfW / inSampleSize >= reqW) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
}
