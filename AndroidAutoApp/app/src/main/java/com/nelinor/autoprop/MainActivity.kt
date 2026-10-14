package com.nelinor.autoprop

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.nelinor.autoprop.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

/**
 * Phone-side Activity.
 *
 * The user pastes an image URL and taps "Send to Car Screen".
 * - The image is previewed on the phone via Glide.
 * - SharedImageState broadcasts the URL to the car screen via Flow.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUrlInput()
        observeSelectedImage()

        // Handle URL passed via intent extra (e.g. from adb or other apps)
        val intentUrl = intent?.getStringExtra("image_url")
        if (!intentUrl.isNullOrBlank()) {
            binding.etImageUrl.setText(intentUrl)
            sendUrlToCar()
        } else if (savedInstanceState == null) {
            // Pre-fill the default URL only on first launch (not on rotation)
            binding.etImageUrl.setText(getString(R.string.default_image_url))
        }
    }

    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent)
        Log.d("AutoGallery", "onNewIntent: ${intent.getStringExtra("image_url")}")
        intent.getStringExtra("image_url")?.let { url ->
            if (url.isNotBlank()) {
                // Directly call SharedImageState — binding may not be ready in background
                SharedImageState.selectUrl(url)
                Log.d("AutoGallery", "onNewIntent: selectUrl($url)")
                // Also update UI if binding is available
                try {
                    binding.etImageUrl.setText(url)
                } catch (_: Exception) {}
            }
        }
    }

    private fun setupUrlInput() {
        // Direct send (no crop)
        binding.btnSendUrl.setOnClickListener {
            sendUrlToCar()
        }

        // Crop & Send — opens CropActivity
        binding.btnCropAndSend.setOnClickListener {
            openCropEditor()
        }

        binding.etImageUrl.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                openCropEditor()
                true
            } else false
        }
    }

    private fun openCropEditor() {
        val url = binding.etImageUrl.text.toString().trim()
        when {
            url.isEmpty() -> {
                Toast.makeText(this, "Please enter an image URL", Toast.LENGTH_SHORT).show()
                return
            }
            !url.startsWith("http://") && !url.startsWith("https://") -> {
                Toast.makeText(this, "URL must start with http:// or https://", Toast.LENGTH_SHORT).show()
                return
            }
        }
        // Hide keyboard
        val imm = getSystemService(INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
        imm.hideSoftInputFromWindow(binding.etImageUrl.windowToken, 0)
        binding.etImageUrl.clearFocus()

        CropActivity.startWithUrl(this, url)
    }

    private fun sendUrlToCar() {
        val url = binding.etImageUrl.text.toString().trim()
        when {
            url.isEmpty() -> {
                Toast.makeText(this, "Please enter an image URL", Toast.LENGTH_SHORT).show()
                return
            }
            !url.startsWith("http://") && !url.startsWith("https://") -> {
                Toast.makeText(this, "URL must start with http:// or https://", Toast.LENGTH_SHORT).show()
                return
            }
        }

        // Send to car
        SharedImageState.selectUrl(url)

        // Show status banner
        val shortName = url.substringAfterLast("/").take(40).ifBlank { "Remote Image" }
        binding.tvSelectedInfo.text = "✓ Sent to car: $shortName"
        binding.tvSelectedInfo.visibility = View.VISIBLE

        // Show image preview on phone using Glide
        binding.ivPreview.visibility = View.VISIBLE
        Glide.with(this)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_close_clear_cancel)
            .into(binding.ivPreview)

        // Hide keyboard
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etImageUrl.windowToken, 0)
        binding.etImageUrl.clearFocus()

        Toast.makeText(this, "Image sent to car screen!", Toast.LENGTH_SHORT).show()
    }

    private fun observeSelectedImage() {
        lifecycleScope.launch {
            SharedImageState.selectedImageUri.collect { uri ->
                if (uri == null) {
                    binding.tvSelectedInfo.visibility = View.GONE
                    binding.ivPreview.visibility = View.GONE
                }
            }
        }
    }
}
