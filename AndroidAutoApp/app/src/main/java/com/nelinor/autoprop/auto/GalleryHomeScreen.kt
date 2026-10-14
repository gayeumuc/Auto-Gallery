package com.nelinor.autoprop.auto

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.net.Uri
import android.util.Log
import androidx.car.app.AppManager
import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.SurfaceCallback
import androidx.car.app.SurfaceContainer
import androidx.car.app.model.Action
import androidx.car.app.model.ActionStrip
import androidx.car.app.model.CarIcon
import androidx.car.app.model.MessageTemplate
import androidx.car.app.model.Template
import androidx.car.app.navigation.model.NavigationTemplate
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

class GalleryHomeScreen(carContext: CarContext) : Screen(carContext) {

    companion object {
        private const val TAG = "AutoGallery"
    }

    private var currentBitmap: Bitmap? = null
    private var isLoading = false
    private var errorMessage: String? = null
    private var surfaceContainer: SurfaceContainer? = null

    private val surfaceCallback = object : SurfaceCallback {
        override fun onSurfaceAvailable(container: SurfaceContainer) {
            Log.d(TAG, "onSurfaceAvailable ${container.width}x${container.height}")
            surfaceContainer = container
            renderFrame()
        }

        override fun onSurfaceDestroyed(container: SurfaceContainer) {
            Log.d(TAG, "onSurfaceDestroyed")
            surfaceContainer = null
        }

        override fun onVisibleAreaChanged(visibleArea: Rect) {
            Log.d(TAG, "onVisibleAreaChanged $visibleArea")
            renderFrame()
        }

        override fun onStableAreaChanged(stableArea: Rect) {
            Log.d(TAG, "onStableAreaChanged $stableArea")
            renderFrame()
        }
    }

    init {
        carContext.getCarService(AppManager::class.java)
            .setSurfaceCallback(surfaceCallback)

        lifecycleScope.launch {
            SharedImageState.imageEvent.collect { event ->
                Log.d(TAG, "imageEvent: ${event.uri} isRemote=${event.isRemote}")
                val uriStr = event.uri.toString()
                if (uriStr.isBlank() || uriStr == "null") {
                    currentBitmap = null
                    isLoading = false
                    errorMessage = "No image selected."
                    invalidate()
                    renderFrame()
                } else {
                    loadBitmap(event.uri, event.isRemote)
                }
            }
        }
    }

    private fun loadBitmap(uri: Uri, isRemote: Boolean) {
        isLoading = true
        errorMessage = null
        invalidate()

        lifecycleScope.launch {
            try {
                val bmp = withContext(Dispatchers.IO) {
                    when {
                        uri.scheme == "inmemory" -> {
                            InMemoryBitmapStore.croppedBitmap
                                ?: throw IllegalStateException("No in-memory bitmap")
                        }
                        isRemote -> downloadBitmap(uri.toString())
                        else -> decodeBitmapFromContent(uri)
                    }
                }
                currentBitmap?.recycle()
                currentBitmap = bmp
                isLoading = false
                errorMessage = null
                Log.d(TAG, "Bitmap loaded: ${bmp.width}x${bmp.height}")
            } catch (e: Exception) {
                currentBitmap = null
                isLoading = false
                errorMessage = "Could not load image: ${e.message}"
                Log.e(TAG, "loadBitmap error", e)
            }
            invalidate()
            renderFrame()
        }
    }

    private fun renderFrame() {
        val container = surfaceContainer ?: return
        val surface = container.surface ?: return
        val bitmap = currentBitmap

        try {
            val canvas: Canvas = surface.lockCanvas(null)
            canvas.drawColor(Color.BLACK)

            if (bitmap != null) {
                val sw = canvas.width.toFloat()
                val sh = canvas.height.toFloat()
                val bw = bitmap.width.toFloat()
                val bh = bitmap.height.toFloat()
                val scale = minOf(sw / bw, sh / bh)
                val dx = (sw - bw * scale) / 2f
                val dy = (sh - bh * scale) / 2f
                val matrix = Matrix()
                matrix.setScale(scale, scale)
                matrix.postTranslate(dx, dy)
                val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
                canvas.drawBitmap(bitmap, matrix, paint)
                Log.d(TAG, "renderFrame: drew ${bw.toInt()}x${bh.toInt()} on ${sw.toInt()}x${sh.toInt()}")
            }

            surface.unlockCanvasAndPost(canvas)
        } catch (e: Exception) {
            Log.e(TAG, "renderFrame error: ${e.message}")
        }
    }

    override fun onGetTemplate(): Template {
        if (isLoading) {
            return MessageTemplate.Builder("Loading image…")
                .setTitle("Auto Prop")
                .build()
        }

        if (currentBitmap == null && surfaceContainer == null) {
            return MessageTemplate.Builder(
                errorMessage
                    ?: "Open Auto Prop on your phone, paste an image URL and tap \"Send to Car Screen\"."
            )
                .setTitle("Auto Prop")
                .build()
        }

        // NavigationTemplate renders on top of the surface — bitmap fills the background
        return NavigationTemplate.Builder()
            .setActionStrip(
                ActionStrip.Builder()
                    .addAction(
                        Action.Builder()
                            .setIcon(
                                CarIcon.Builder(
                                    IconCompat.createWithResource(
                                        carContext,
                                        android.R.drawable.ic_menu_gallery
                                    )
                                ).build()
                            )
                            .setOnClickListener { /* no-op */ }
                            .build()
                    )
                    .build()
            )
            .build()
    }

    private fun downloadBitmap(url: String): Bitmap {
        val conn = URL(url).openConnection() as HttpURLConnection
        conn.connectTimeout = 10_000
        conn.readTimeout = 15_000
        conn.instanceFollowRedirects = true
        conn.connect()
        val stream: InputStream = conn.inputStream
        val bmp = BitmapFactory.decodeStream(stream)
        stream.close()
        conn.disconnect()
        return bmp
    }

    private fun decodeBitmapFromContent(uri: Uri): Bitmap {
        return carContext.contentResolver.openInputStream(uri)?.use { stream ->
            BitmapFactory.decodeStream(stream)
        } ?: throw IllegalStateException("Cannot open URI: $uri")
    }
}
