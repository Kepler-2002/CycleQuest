package com.cyclequest.data.sync.workers

import com.cyclequest.core.database.sync.ConflictStrategy
import com.cyclequest.core.database.sync.SyncStatus
import com.cyclequest.core.network.ApiResult
import com.cyclequest.data.local.dao.UserDao
import com.cyclequest.data.sync.Apis.UserApi
import javax.inject.Inject

class UserSyncWorker @Inject constructor(
    private val userDao: UserDao,
    private val userApi: UserApi
) : SyncWorker {
    override val syncType = SyncType.User

    override suspend fun sync() {
        val localUsers = userDao.getAllUsers()

        /* 禁用同步功能，仅返回本地数据

        when (val result = userApi.getUsers()) {
            is ApiResult.Success -> {
                val remoteUsers = result.data
                when (syncType.conflictStrategy) {
                    ConflictStrategy.SERVER_WINS -> {
                        userDao.upsertAll(remoteUsers.map { it.copy(syncStatus = SyncStatus.SYNCED) })
                    }
                    ConflictStrategy.CLIENT_WINS -> {
                        val pendingUsers = localUsers.filter { it.syncStatus == SyncStatus.PENDING }
                        pendingUsers.forEach { user ->
                            when (val updateResult = userApi.updateUser(user)) {
                                is ApiResult.Success -> {
                                    userDao.updateUser(updateResult.data.copy(syncStatus = SyncStatus.SYNCED))
                                }
                                is ApiResult.Error -> {
                                    userDao.updateUser(user.copy(syncStatus = SyncStatus.ERROR))
                                }
                                is ApiResult.Loading -> { /* 忽略加载状态 */ }
                            }
                        }
                        
                        // 更新其他非待同步数据
                        val nonPendingRemoteUsers = remoteUsers.filter { remote ->
                            localUsers.none { local -> 
                                local.id == remote.id && local.syncStatus == SyncStatus.PENDING 
                            }
                        }
                        userDao.upsertAll(nonPendingRemoteUsers.map { it.copy(syncStatus = SyncStatus.SYNCED) })
                    }
                    ConflictStrategy.LAST_MODIFIED_WINS -> TODO()
                }
            }
            is ApiResult.Error -> {
                // TODO
            }
            is ApiResult.Loading -> { /* 忽略加载状态 */ }
        }*/

        //将所有本地用户标记为 已同步 -- 已注释掉 syncStatus
        //userDao.upsertAll(localUsers.map { it.copy(syncStatus = SyncStatus.SYNCED)})
    }
}
