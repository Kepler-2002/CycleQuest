package com.cyclequest.core.database.sync

import kotlin.math.pow
import kotlin.time.Duration

sealed class RetryPolicy {
    abstract val initialDelay: Duration
    abstract val maxAttempts: Int
    
    fun getNextDelay(attempt: Int): Duration {
        return when (this) {
            is Exponential -> initialDelay * multiplier.pow(attempt - 1)
            is Linear -> initialDelay
            NoRetry -> Duration.ZERO
        }
    }
    
    fun shouldRetry(attempt: Int): Boolean = attempt < maxAttempts

    data class Exponential(
        override val initialDelay: Duration,
        override val maxAttempts: Int,
        val multiplier: Double = 2.0
    ) : RetryPolicy()

    data class Linear(
        override val initialDelay: Duration,
        override val maxAttempts: Int
    ) : RetryPolicy()

    object NoRetry : RetryPolicy() {
        override val initialDelay: Duration = Duration.ZERO
        override val maxAttempts: Int = 0
    }
}
