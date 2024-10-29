package com.cyclequest.core.database.sync

import SyncType
import android.content.SharedPreferences
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseSyncImpl @Inject constructor(
    private val syncConfig: SyncConfig,
    private val sharedPreferences: SharedPreferences
) : DatabaseSync {
    override suspend fun shouldSync(syncType: SyncType): Boolean {
        val lastSyncTime = sharedPreferences.getLong(LAST_SYNC_TIME_KEY, 0)
        return System.currentTimeMillis() - lastSyncTime >= syncConfig.syncInterval.inWholeMilliseconds
    }

    override suspend fun markSynced(syncType: SyncType) {
        sharedPreferences.edit().putLong(LAST_SYNC_TIME_KEY, System.currentTimeMillis()).apply()
    }

    override suspend fun markError(syncType: SyncType, error: Throwable) {
        // 可以记录错误日志或更新同步状态
        Timber.e(error, "Sync error occurred")
    }

    companion object {
        private const val LAST_SYNC_TIME_KEY = "last_sync_time"
    }
}
