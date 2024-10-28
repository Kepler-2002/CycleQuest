package com.cyclequest.data.sync

import SyncType
import com.cyclequest.core.database.sync.DatabaseSync
import com.cyclequest.core.database.sync.SyncConfig
import com.cyclequest.core.database.sync.SyncStatus
import com.cyclequest.data.sync.workers.SyncWorker
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncManager @Inject constructor(
    private val databaseSync: DatabaseSync,
    private val syncWorkers: Set<@JvmSuppressWildcards SyncWorker>,
    private val syncScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) {
    private val _syncStatus = MutableStateFlow<Map<SyncType, SyncStatus>>(emptyMap())
    val syncStatus: Flow<Map<SyncType, SyncStatus>> = _syncStatus

    fun startSync(syncType: SyncType) {
        syncScope.launch {
            updateSyncStatus(syncType, SyncStatus.SYNCING)
            try {
                syncWorkers.find { it.syncType == syncType }?.sync()
                databaseSync.markSynced(syncType)
                updateSyncStatus(syncType, SyncStatus.SYNCED)
            } catch (e: Exception) {
                databaseSync.markError(syncType, e)
                updateSyncStatus(syncType, SyncStatus.ERROR)
            }
        }
    }
    fun startSync() {
        syncScope.launch {
            syncWorkers.forEach { worker ->
                startSync(worker.syncType)
            }
        }
    }

    private fun updateSyncStatus(syncType: SyncType, status: SyncStatus) {
        _syncStatus.value = _syncStatus.value.toMutableMap().apply {
            put(syncType, status)
        }
    }

    fun schedulePeriodicSync() {
        syncWorkers.forEach { worker ->
            scheduleWorkerSync(worker)
        }
    }

    private fun scheduleWorkerSync(worker: SyncWorker) {
        syncScope.launch {
            while (true) {
                delay(worker.syncType.syncInterval.inWholeMilliseconds)
                if (databaseSync.shouldSync(worker.syncType)) {
                    startSync(worker.syncType)
                }
            }
        }
    }
}
