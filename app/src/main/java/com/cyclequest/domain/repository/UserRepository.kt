package com.cyclequest.domain.repository

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
    private val syncManager: SyncManager
) {
    // 获取所有用户，返回 Flow
    fun getUsers(): Flow<List<User>> {
        return userDao.getAllUsersFlow().map { users ->
            users.map { userMapper.toDomain(it) }
        }
    }

    // 获取单个用户
    suspend fun getUser(id: String): User? {
        return userDao.getUserById(id)?.let { userMapper.toDomain(it) }
    }

    // 强制刷新用户数据
    suspend fun refreshUsers() {
        syncManager.startSync()
    }

    // 更新用户信息
    suspend fun updateUser(user: User) {
        val localUser = userMapper.toLocal(user).copy(syncStatus = SyncStatus.PENDING)
        userDao.updateUser(localUser)
        syncManager.startSync()
    }

    // 删除用户
    suspend fun deleteUser(userId: String) {
        userDao.deleteUser(userId)
        syncManager.startSync()
    }

    // 创建新用户
    suspend fun createUser(user: User) {
        val localUser = userMapper.toLocal(user).copy(syncStatus = SyncStatus.PENDING)
        userDao.insertUser(localUser)
        syncManager.startSync()
    }
}
