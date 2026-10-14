package com.nelinor.autoprop

import android.graphics.Bitmap

/**
 * Simple in-process singleton that holds the latest cropped bitmap.
 *
 * Because MainActivity and MediaCarAppService run in the same process,
 * this avoids all FileProvider / URI permission issues when sharing
 * a cropped bitmap from the phone UI to the car screen.
 */
object InMemoryBitmapStore {
    @Volatile
    var croppedBitmap: Bitmap? = null
        private set

    /** Store a new cropped bitmap. Returns a unique token so StateFlow always emits. */
    fun store(bitmap: Bitmap): Long {
        croppedBitmap?.recycle()
        croppedBitmap = bitmap
        return System.currentTimeMillis()
    }

    fun clear() {
        croppedBitmap?.recycle()
        croppedBitmap = null
    }
}
