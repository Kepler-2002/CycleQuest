package com.cyclequest.core.initializer

import android.app.Application
import com.amap.api.maps2d.MapsInitializer
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

// core/initializer/MapInitializer.kt
@Singleton
class MapInitializer @Inject constructor(
) {
    fun initialize(application: Application) {
        MapsInitializer.initialize(application)
        Timber.d("Map SDK initialized")
    }
}