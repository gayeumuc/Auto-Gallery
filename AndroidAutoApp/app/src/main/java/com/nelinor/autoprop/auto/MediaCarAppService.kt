package com.nelinor.autoprop.auto

import androidx.car.app.CarAppService
import androidx.car.app.Session
import androidx.car.app.validation.HostValidator

/**
 * The entry point for the Android Auto side of the app.
 * Android Auto binds to this service when the phone is connected to a car head unit.
 *
 * IMPORTANT: androidx.car.app.minCarApiLevel must be declared as <meta-data>
 * inside this <service> tag in AndroidManifest.xml — not at the <application> level.
 */
class MediaCarAppService : CarAppService() {

    override fun createHostValidator(): HostValidator {
        return HostValidator.ALLOW_ALL_HOSTS_VALIDATOR
    }

    override fun onCreateSession(): Session {
        return MediaSession()
    }
}
