package com.cyclequest.core.database.sync

import SyncType

// 定义一些sync动作
// core/database/sync/DatabaseSync.kt
interface DatabaseSync {
    suspend fun shouldSync(syncType: SyncType): Boolean
    suspend fun markSynced(syncType: SyncType)
    suspend fun markError(syncType: SyncType, error: Throwable)
}