package com.cyclequest.data.sync.workers

import SyncType

// data/sync/SyncWorker.kt
interface SyncWorker {
    val syncType: SyncType
    suspend fun sync()
}
