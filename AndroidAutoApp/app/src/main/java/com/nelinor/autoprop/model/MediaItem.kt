package com.nelinor.autoprop.model

import android.net.Uri

/**
 * Represents a single image from the device gallery.
 */
data class MediaItem(
    val id: Long,
    val uri: Uri,
    val displayName: String,
    val dateTaken: Long,
    val width: Int = 0,
    val height: Int = 0
)
