package com.cyclequest.core.initializer

import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

// core/initializer/ServiceInitializer.kt
@Singleton
class ServiceInitializer @Inject constructor() {
    fun initialize() {
        Timber.d("Services initialized")
    }
}