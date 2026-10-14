package com.nelinor.autoprop

import android.graphics.Bitmap
import android.net.Uri
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Singleton bridge between the phone UI and the Android Auto car screens.
 *
 * Supports two image sources:
 *  1. Local gallery image (selected via MediaStore) — stored as a content:// URI
 *  2. Remote image URL (typed/pasted by the user) — stored as an https:// URI
 *
 * Both the phone Activity and the CarAppService run in the same process,
 * so they share this in-memory singleton without any IPC overhead.
 *
 * Uses MutableSharedFlow for image events so that the same URI can be sent
 * multiple times and always triggers a reload (unlike StateFlow which deduplicates).
 */
object SharedImageState {

    // SharedFlow emits every time a new image is selected — even if the URI is the same.
    // replay=0: no buffered events replayed on new collectors (avoids stale image on reconnect)
    // extraBufferCapacity=1: buffers one event so tryEmit never drops when collector is briefly busy
    // replay=1: new collectors (e.g. ImageDetailScreen opened after send) get the last event immediately
    private val _imageEvent = MutableSharedFlow<ImageEvent>(replay = 1, extraBufferCapacity = 1)
    val imageEvent: SharedFlow<ImageEvent> = _imageEvent.asSharedFlow()

    // Convenience StateFlow for UI components that only need the current URI
    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri: StateFlow<Uri?> = _selectedImageUri.asStateFlow()

    // The ID of the local gallery image (-1 if URL-based)
    private val _selectedImageId = MutableStateFlow<Long>(-1L)
    val selectedImageId: StateFlow<Long> = _selectedImageId.asStateFlow()

    // Display name shown in the car header
    val selectedImageName = MutableStateFlow<String>("")

    // Whether the current source is a remote URL (vs local gallery)
    val isRemoteUrl = MutableStateFlow<Boolean>(false)

    data class ImageEvent(
        val uri: Uri,
        val name: String,
        val isRemote: Boolean,
        val id: Long = -1L,
        val timestamp: Long = System.currentTimeMillis() // ensures uniqueness even for same URI
    )

    /** Called when the user taps a local gallery image. */
    fun selectImage(id: Long, uri: Uri, name: String) {
        _selectedImageId.value = id
        _selectedImageUri.value = uri
        selectedImageName.value = name
        isRemoteUrl.value = false
        _imageEvent.tryEmit(ImageEvent(uri = uri, name = name, isRemote = false, id = id))
    }

    /** Called when the user enters a remote image URL. */
    fun selectUrl(url: String) {
        val uri = Uri.parse(url)
        val name = url.substringAfterLast("/").take(40).ifBlank { "Remote Image" }
        _selectedImageId.value = -1L
        _selectedImageUri.value = uri
        selectedImageName.value = name
        isRemoteUrl.value = true
        _imageEvent.tryEmit(ImageEvent(uri = uri, name = name, isRemote = true))
    }

    /**
     * Called when the user crops/edits a bitmap on the phone.
     *
     * Stores the bitmap in InMemoryBitmapStore (same process as the car service)
     * and emits a unique inmemory://crop/<timestamp> URI so StateFlow always fires
     * even when the same image is cropped multiple times.
     */
    fun selectBitmap(bitmap: Bitmap, name: String = "Cropped Image") {
        val token = InMemoryBitmapStore.store(bitmap)
        // Unique URI every call — avoids StateFlow deduplication
        val uri = Uri.parse("inmemory://crop/$token")
        _selectedImageId.value = -1L
        _selectedImageUri.value = uri
        selectedImageName.value = name
        isRemoteUrl.value = false
        _imageEvent.tryEmit(ImageEvent(uri = uri, name = name, isRemote = false))
    }

    /** Clears the current selection. */
    fun clearSelection() {
        _selectedImageId.value = -1L
        _selectedImageUri.value = null
        selectedImageName.value = ""
        isRemoteUrl.value = false
    }

    val hasSelection: Boolean
        get() = _selectedImageUri.value != null
}
