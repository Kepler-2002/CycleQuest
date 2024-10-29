package com.cyclequest.core.database.sync

import kotlin.time.Duration

// core/database/sync/SyncConfig.kt
interface SyncConfig {
    val syncInterval: Duration
    val retryPolicy: RetryPolicy
    val conflictStrategy: ConflictStrategy
}