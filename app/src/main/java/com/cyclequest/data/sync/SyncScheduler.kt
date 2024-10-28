package com.cyclequest.data.sync

import androidx.work.*
import com.cyclequest.core.database.sync.SyncConfig
import com.cyclequest.data.sync.workers.SyncWorkerImpl
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncScheduler @Inject constructor(
    private val workManager: WorkManager,
    private val syncConfig: SyncConfig
) {
    fun schedulePeriodicalSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = PeriodicWorkRequestBuilder<SyncWorkerImpl>(
            repeatInterval = syncConfig.syncInterval.inWholeMinutes,
            repeatIntervalTimeUnit = TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                syncConfig.retryPolicy.initialDelay.inWholeMilliseconds,
                TimeUnit.MILLISECONDS
            )
            .build()

        workManager.enqueueUniquePeriodicWork(
            SYNC_WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            syncRequest
        )
    }

    companion object {
        private const val SYNC_WORK_NAME = "periodic_sync"
    }
}
