package com.cyclequest.data.local.dao

import com.cyclequest.data.local.TestDatabase
import com.cyclequest.data.local.entity.UserAchievementEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

class UserAchievementDaoTest : TestDatabase() {
    private val userAchievementDao by lazy { getDatabase().userAchievementDao() }

    @Test
    fun getUserAchievements() = runBlocking {
        println("=== 测试获取用户成就 ===")
        val userAchievements = listOf(
            createTestUserAchievement("user1", "achievement1"),
            createTestUserAchievement("user1", "achievement2")
        )
        println("创建测试用户成就列表: ${userAchievements.joinToString()}")
        
        userAchievements.forEach { 
            println("插入用户成就: $it")
            userAchievementDao.insertUserAchievement(it) 
        }
        println("所有用户成就已插入数据库")
        
        val loadedAchievements = userAchievementDao.getUserAchievementsFlow("user1").first()
        println("从数据库加载的用户成就列表: ${loadedAchievements.joinToString()}")
        assertEquals(userAchievements.toSet(), loadedAchievements.toSet())
        println("=== 测试完成 ===")
    }

    private fun createTestUserAchievement(
        userId: String = "test_user",
        achievementId: String = "test_achievement"
    ) = UserAchievementEntity(
        userId = userId,
        achievementId = achievementId,
        unlockedAt = System.currentTimeMillis(),
        progress = 0.0
    )
} 