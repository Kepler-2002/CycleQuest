package com.cyclequest.data.local.dao

import com.cyclequest.data.local.TestDatabase
import com.cyclequest.data.local.entity.UserEntity
import com.cyclequest.data.local.entity.UserStatus
import com.cyclequest.core.database.sync.SyncStatus
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.Assert.*

class UserDaoTest : TestDatabase() {
    private val userDao by lazy { getDatabase().userDao() }

    @Test
    fun insertAndGetUser() = runBlocking {
        println("=== 测试插入和获取用户 ===")
        val user = createTestUser()
        println("创建测试用户: $user")
        userDao.insertUser(user)
        println("用户已插入数据库")
        
        val loadedUser = userDao.getUserById(user.id)
        println("从数据库加载用户: $loadedUser")
        assertEquals(user, loadedUser)
        println("=== 测试完成 ===")
    }

    @Test
    fun deleteUser() = runBlocking {
        println("=== 测试删除用户 ===")
        val user = createTestUser()
        println("创建测试用户: $user")
        userDao.insertUser(user)
        println("用户已插入数据库")
        userDao.deleteUser(user.id)
        println("用户已从数据库删除")
        
        val loadedUser = userDao.getUserById(user.id)
        println("尝试加载已删除用户: $loadedUser")
        assertNull(loadedUser)
        println("=== 测试完成 ===")
    }

    @Test
    fun getAllUsersFlow() = runBlocking {
        println("=== 测试获取所有用户流 ===")
        val users = listOf(
            createTestUser("1"),
            createTestUser("2"),
            createTestUser("3")
        )
        println("创建测试用户列表: ${users.joinToString()}")
        
        users.forEach { 
            println("插入用户: $it")
            userDao.insertUser(it) 
        }
        println("所有用户已插入数据库")
        
        val loadedUsers = userDao.getAllUsersFlow().first()
        println("从数据库加载的用户列表: ${loadedUsers.joinToString()}")
        assertEquals(users.toSet(), loadedUsers.toSet())
        println("=== 测试完成 ===")
    }

    private fun createTestUser(id: String = "test_id") = UserEntity(
        id = id,
        username = "test_user",
        email = "test@example.com",
        phoneNumber = "1234567890",
        password = "password",
        avatarUrl = null,
        status = UserStatus.ACTIVE,
        totalRides = 0,
        totalDistance = 0f,
        totalRideTime = 0L,
        lastLoginAt = null,
        // syncStatus = SyncStatus.PENDING,
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis()
    )
} 