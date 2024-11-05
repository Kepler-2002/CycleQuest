package com.cyclequest.domain.repository

import com.cyclequest.core.database.sync.DatabaseSync
import com.cyclequest.core.database.sync.SyncStatus
import com.cyclequest.data.local.dao.UserDao
import com.cyclequest.data.mapper.UserMapper
import com.cyclequest.data.sync.SyncManager
import com.cyclequest.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val userMapper: UserMapper,
    private val syncManager: SyncManager,
    private val databaseSync: DatabaseSync
) {
    fun getUsers(): Flow<List<User>> {
        return userDao.getAllUsersFlow()
            .map { users -> users.map(userMapper::toDomain) }
    }

    suspend fun getUser(id: String): User? {
        if (databaseSync.shouldSync(SyncType.User)) {
            syncManager.startSync()
        }
        return userDao.getUserById(id)?.let { userMapper.toDomain(it) }
    }

    suspend fun updateUser(user: User) {
        val localUser = userMapper.toLocal(user).copy(syncStatus = SyncStatus.PENDING)
        userDao.updateUser(localUser)
        syncManager.startSync()
    }

    suspend fun refreshUsers() {
        syncManager.startSync()
    }

    fun UserRegister(){
        //1. 从云端 同步User （UserID）
        // 2. 从UserDao里面找有没有已经注册过的UserID

        // 3. 如果不重复，就写UserDao
        // 4. 同步云端
    }
}
