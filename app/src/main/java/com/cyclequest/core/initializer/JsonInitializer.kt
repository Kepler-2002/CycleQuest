package com.cyclequest.core.initializer

import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import com.google.gson.Gson

// core/initializer/LoggingInitializer.kt
@Singleton
class JsonInitializer @Inject constructor(
    private val Gson : Gson
) {
    fun initialize() {

        Timber.d("Logging initialized")
    }
}