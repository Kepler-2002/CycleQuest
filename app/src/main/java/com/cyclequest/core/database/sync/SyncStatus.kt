package com.cyclequest.core.database.sync

import androidx.room.Entity
import kotlin.time.Duration

// core/database/base/SyncStatus.kt
enum class SyncStatus {
    PENDING,
    SYNCING,
    SYNCED,
    ERROR,
    Idle
}

