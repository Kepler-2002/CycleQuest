package com.cyclequest.data.local.dao

import com.cyclequest.data.local.TestDatabase
import com.cyclequest.data.local.entity.UserExploredRegionEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class UserExploredRegionDaoTest : TestDatabase() {
    private val userExploredRegionDao by lazy { getDatabase().userExploredRegionDao() }

    @Test
    fun insertAndGetUserExploredRegions() = runBlocking {
        println("=== 测试插入和获取用户探索区域 ===")
        val regions = listOf(
            createTestUserExploredRegion("user1", "region1"),
            createTestUserExploredRegion("user1", "region2")
        )
        println("创建测试探索区域列表: ${regions.joinToString()}")
        
        regions.forEach { 
            println("插入探索区域: $it")
            userExploredRegionDao.insertUserExploredRegion(it)
        }
        println("所有探索区域已插入数据库")
        
        val loadedRegions = userExploredRegionDao.getUserExploredRegionsFlow("user1").first()
        println("从数据库加载的探索区域列表: ${loadedRegions.joinToString()}")
        assertEquals(2, loadedRegions.size)
        println("=== 测试完成 ===")
    }

//    @Test
//    fun updateLastExploreTime() = runBlocking {
//        println("=== 测试更新最后访问时间 ===")
//        val region = createTestUserExploredRegion("user1", "region1")
//        println("创建测试探索区域: $region")
//        userExploredRegionDao.insertUserExploredRegion(region)
//        println("探索区域已插入数据库")
//
//        val newTimestamp = System.currentTimeMillis()
//        userExploredRegionDao.updateLastVisitTime("user1", "region1", newTimestamp)
//        println("更新最后探索时间")
//
//        val updatedRegion = userExploredRegionDao.getUserExploredRegion("user1", "region1")
//        println("更新后的探索区域: $updatedRegion")
//        assertEquals(newTimestamp, updatedRegion?.lastExploredTime)
//        println("=== 测试完成 ===")
//    }

    private fun createTestUserExploredRegion(
        userId: String,
        regionCode: String
    ) = UserExploredRegionEntity(
        userId = userId,
        regionCode = regionCode,
        firstExploredTime = System.currentTimeMillis(),
        lastExploredTime = System.currentTimeMillis(),
        exploredCount = 1
    )
} 