package com.cyclequest.core.initializer

import android.app.Application
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppInitializer @Inject constructor(
    private val application: Application,
    private val loggingInitializer: LoggingInitializer,
    private val mapInitializer: MapInitializer,
    private val databaseInitializer: DatabaseInitializer
) {
    fun initialize() {
        loggingInitializer.initialize()
        mapInitializer.initialize(application)
        databaseInitializer.initialize()
    }
}
