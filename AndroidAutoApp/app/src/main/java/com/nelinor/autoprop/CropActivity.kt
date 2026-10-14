package com.nelinor.autoprop

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.nelinor.autoprop.databinding.ActivityCropBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * Phone-side crop/zoom editor.
 *
 * Receives either:
 *   - EXTRA_IMAGE_URL  (String) — remote URL to download
 *   - EXTRA_IMAGE_URI  (String) — local content:// URI
 *
 * The user pans/zooms the image inside a 16:9 crop window.
 * "Send to Car" crops the bitmap and calls SharedImageState.selectBitmap().
 */
class CropActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "AutoGallery"
        const val EXTRA_IMAGE_URL = "crop_image_url"
        const val EXTRA_IMAGE_URI = "crop_image_uri"

        fun startWithUrl(context: Context, url: String) {
            context.startActivity(Intent(context, CropActivity::class.java).apply {
                putExtra(EXTRA_IMAGE_URL, url)
            })
        }

        fun startWithUri(context: Context, uri: Uri) {
            context.startActivity(Intent(context, CropActivity::class.java).apply {
                putExtra(EXTRA_IMAGE_URI, uri.toString())
            })
        }
    }

    private lateinit var binding: ActivityCropBinding
    private var currentBitmap: Bitmap? = null
    private var rotationDegrees = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCropBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        binding.btnReset.setOnClickListener {
            currentBitmap?.let { binding.cropView.setBitmap(it) }
        }

        binding.btnRotate.setOnClickListener {
            rotateCurrentBitmap()
        }

        binding.btnSendCropped.setOnClickListener {
            sendCroppedTocar()
        }

        // Load image
        val url = intent.getStringExtra(EXTRA_IMAGE_URL)
        val uriStr = intent.getStringExtra(EXTRA_IMAGE_URI)
        when {
            url != null -> loadFromUrl(url)
            uriStr != null -> loadFromUri(Uri.parse(uriStr))
            else -> {
                Toast.makeText(this, "No image source provided", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun loadFromUrl(url: String) {
        showLoading(true)
        lifecycleScope.launch {
            try {
                val bmp = withContext(Dispatchers.IO) { downloadBitmap(url) }
                currentBitmap = bmp
                binding.cropView.setBitmap(bmp)
                Log.d(TAG, "CropActivity: loaded from URL ${bmp.width}x${bmp.height}")
            } catch (e: Exception) {
                Log.e(TAG, "CropActivity: load error ${e.message}", e)
                Toast.makeText(this@CropActivity, "Could not load image: ${e.message}", Toast.LENGTH_LONG).show()
                finish()
            } finally {
                showLoading(false)
            }
        }
    }

    private fun loadFromUri(uri: Uri) {
        showLoading(true)
        lifecycleScope.launch {
            try {
                val bmp = withContext(Dispatchers.IO) {
                    contentResolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it) }
                        ?: throw IllegalStateException("Cannot open URI: $uri")
                }
                currentBitmap = bmp
                binding.cropView.setBitmap(bmp)
                Log.d(TAG, "CropActivity: loaded from URI ${bmp.width}x${bmp.height}")
            } catch (e: Exception) {
                Log.e(TAG, "CropActivity: load error ${e.message}", e)
                Toast.makeText(this@CropActivity, "Could not load image: ${e.message}", Toast.LENGTH_LONG).show()
                finish()
            } finally {
                showLoading(false)
            }
        }
    }

    private fun rotateCurrentBitmap() {
        val bmp = currentBitmap ?: return
        rotationDegrees = (rotationDegrees + 90) % 360
        val matrix = Matrix().apply { postRotate(90f) }
        val rotated = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true)
        currentBitmap = rotated
        binding.cropView.setBitmap(rotated)
    }

    private fun sendCroppedTocar() {
        showLoading(true)
        lifecycleScope.launch {
            try {
                val cropped = withContext(Dispatchers.Default) {
                    binding.cropView.getCroppedBitmap()
                }
                SharedImageState.selectBitmap(cropped, "Cropped Photo")
                Log.d(TAG, "CropActivity: sent cropped ${cropped.width}x${cropped.height}")
                Toast.makeText(this@CropActivity, "Sent to car screen!", Toast.LENGTH_SHORT).show()
                finish()
            } catch (e: Exception) {
                Log.e(TAG, "CropActivity: crop error ${e.message}", e)
                Toast.makeText(this@CropActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                showLoading(false)
            }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.btnSendCropped.isEnabled = !show
        binding.btnRotate.isEnabled = !show
        binding.btnReset.isEnabled = !show
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
        return bmp ?: throw IllegalStateException("BitmapFactory returned null for $url")
    }
}
