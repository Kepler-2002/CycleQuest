package com.cyclequest.data.local.dao

import com.cyclequest.data.local.TestDatabase
import com.cyclequest.data.local.entity.AchievementEntity
import com.cyclequest.data.local.entity.AchievementType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AchievementDaoTest : TestDatabase() {
    private val achievementDao by lazy { getDatabase().achievementDao() }

    @Test
    fun insertAndGetAchievement() = runBlocking {
        println("=== 测试插入和获取成就 ===")
        val achievement = createTestAchievement()
        println("创建测试成就: $achievement")
        achievementDao.insertAchievement(achievement)
        println("成就已插入数据库")
        
        val loadedAchievement = achievementDao.getAchievementById(achievement.id)
        println("从数据库加载成就: $loadedAchievement")
        assertEquals(achievement, loadedAchievement)
        println("=== 测试完成 ===")
    }

    @Test
    fun getAllAchievementsFlow() = runBlocking {
        println("=== 测试获取所有成就流 ===")
        val achievements = listOf(
            createTestAchievement("1"),
            createTestAchievement("2"),
            createTestAchievement("3")
        )
        println("创建测试成就列表: ${achievements.joinToString()}")
        
        println("插入所有成就")
        achievementDao.insertAll(achievements)
        println("所有成就已插入数据库")
        
        val loadedAchievements = achievementDao.getAllAchievementsFlow().first()
        println("从数据库加载的成就列表: ${loadedAchievements.joinToString()}")
        assertEquals(achievements.toSet(), loadedAchievements.toSet())
        println("=== 测试完成 ===")
    }

    private fun createTestAchievement(id: String = "test_id") = AchievementEntity(
        id = id,
        name = "Test Achievement",
        description = "Test Description",
        type = AchievementType.TOTAL_DISTANCE,
        requirement = 100.0,
        iconUrl = "test_icon.png"
    )
} 