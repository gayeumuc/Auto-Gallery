package com.nelinor.autoprop.auto

import android.content.Intent
import androidx.car.app.Screen
import androidx.car.app.Session

/**
 * The Car App Session. Created once per connection to the car head unit.
 * Returns [GalleryHomeScreen] as the initial screen shown on the car display.
 */
class MediaSession : Session() {

    override fun onCreateScreen(intent: Intent): Screen {
        return GalleryHomeScreen(carContext)
    }
}
