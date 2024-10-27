package com.cyclequest.data.sync.workers

import com.cyclequest.core.database.sync.ConflictStrategy
import com.cyclequest.core.database.sync.SyncStatus
import com.cyclequest.data.local.dao.UserDao
import com.cyclequest.data.local.entity.UserEntity
import com.cyclequest.data.sync.adapters.DefaultSyncAdapter
import com.cyclequest.service.backend.BackendService
import javax.inject.Inject

class UserSyncWorker @Inject constructor(
    private val userDao: UserDao,
    backendService: BackendService
) : SyncWorker {
    override val syncType = SyncType.User
    override val syncAdapter = DefaultSyncAdapter.create(backendService, UserEntity::class.java)

    override suspend fun sync() {
        val localUsers = userDao.getAllUsers()
        val remoteUsers = syncAdapter.getAll()

        when (syncType.conflictStrategy) {
            ConflictStrategy.SERVER_WINS -> {
                userDao.upsertAll(remoteUsers)
            }
            ConflictStrategy.CLIENT_WINS -> {
                val pendingUsers = localUsers.filter { it.syncStatus == SyncStatus.PENDING }
                val updatedUsers = syncAdapter.update(pendingUsers)
                userDao.upsertAll(updatedUsers)
            }

            ConflictStrategy.LAST_MODIFIED_WINS -> TODO()
        }
    }
}
