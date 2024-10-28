package com.cyclequest.core.initializer

import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

// core/initializer/LoggingInitializer.kt
@Singleton
class LoggingInitializer @Inject constructor(
    private val timberTree: Timber.Tree
) {
    fun initialize() {
        Timber.plant(timberTree)
        Timber.d("Logging initialized")
    }
}