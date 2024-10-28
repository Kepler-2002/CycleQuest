package com.cyclequest.data.sync.workers

import SyncType
import com.cyclequest.core.base.BaseEntity
import com.cyclequest.core.database.sync.SyncConfig
import com.cyclequest.data.sync.adapters.SyncAdapter

// data/sync/SyncWorker.kt
interface SyncWorker {
    val syncType: SyncType
    val syncAdapter: SyncAdapter<out BaseEntity>
    suspend fun sync()
}
