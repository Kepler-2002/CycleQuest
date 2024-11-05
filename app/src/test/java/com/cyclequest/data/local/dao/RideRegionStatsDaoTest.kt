package com.cyclequest.data.local.dao

import com.cyclequest.data.local.TestDatabase
import com.cyclequest.data.local.entity.RideRegionStatsEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class RideRegionStatsDaoTest : TestDatabase() {
    private val rideRegionStatsDao by lazy { getDatabase().rideRegionStatsDao() }

    @Test
    fun insertAndGetRideRegionStats() = runBlocking {
        println("=== 测试插入和获取骑行区域统计 ===")
        val stats = createTestRideRegionStats("user1", "region1")
        println("创建测试统计: $stats")
        rideRegionStatsDao.insertRideRegionStats(stats)
        println("统计已插入数据库")
        
        val loadedStats = rideRegionStatsDao.getRideRegionStatsFlow("user1", "region1").first()
        println("从数据库加载的统计: $loadedStats")
        assertNotNull(loadedStats)
        assertEquals(stats.copy(id = loadedStats.id), loadedStats)
        println("=== 测试完成 ===")
    }

    @Test
    fun updateRideRegionStats() = runBlocking {
        println("=== 测试更新骑行区域统计 ===")
        val stats = createTestRideRegionStats("user1", "region1")
        println("创建初始统计: $stats")
        rideRegionStatsDao.insertRideRegionStats(stats)
        
        val timestamp = System.currentTimeMillis()
        println("更新统计数据")
        rideRegionStatsDao.updateRideRegionStats(
            userId = "user1",
            regionCode = "region1",
            distance = 5.0f,
            duration = 300,
            timestamp = timestamp
        )
        
        val updatedStats = rideRegionStatsDao.getRideRegionStatsFlow("user1", "region1").first()
        println("更新后的统计: $updatedStats")
        assertNotNull(updatedStats)
        assertEquals(15.0f, updatedStats.totalDistance)  // 10 + 5
        assertEquals(1800, updatedStats.totalDuration)   // 1500 + 300
        assertEquals(2, updatedStats.totalRides)         // 1 + 1
        println("=== 测试完成 ===")
    }

    private fun createTestRideRegionStats(
        userId: String,
        regionCode: String
    ) = RideRegionStatsEntity(
        userId = userId,
        regionCode = regionCode,
        totalDistance = 10.0f,
        totalDuration = 1500,
        totalRides = 1,
        lastUpdated = System.currentTimeMillis()
    )
} 