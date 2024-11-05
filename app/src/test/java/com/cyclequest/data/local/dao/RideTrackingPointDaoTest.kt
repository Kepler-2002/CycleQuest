package com.cyclequest.data.local.dao

import com.cyclequest.data.local.TestDatabase
import com.cyclequest.data.local.entity.RideTrackingPointEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RideTrackingPointDaoTest : TestDatabase() {
    private val rideTrackingPointDao by lazy { getDatabase().rideTrackingPointDao() }

    @Test
    fun insertAndGetTrackingPoints() = runBlocking {
        println("=== 测试插入和获取骑行轨迹点 ===")
        val points = listOf(
            createTestTrackingPoint("ride1", 0),
            createTestTrackingPoint("ride1", 1),
            createTestTrackingPoint("ride1", 2)
        )
        println("创建测试轨迹点列表: ${points.joinToString()}")
        
        rideTrackingPointDao.insertTrackingPoints(points)
        println("所有轨迹点已插入数据库")
        
        val loadedPoints = rideTrackingPointDao.getRideTrackingPointsFlow("ride1").first()
        println("从数据库加载的轨迹点列表: ${loadedPoints.joinToString()}")
        
        val expectedPoints = points.map { it.copy(id = 0) }.toSet()
        val actualPoints = loadedPoints.map { it.copy(id = 0) }.toSet()
        assertEquals(expectedPoints, actualPoints)
        println("=== 测试完成 ===")
    }

    @Test
    fun getNavigationPoints() = runBlocking {
        println("=== 测试获取导航点 ===")
        val points = listOf(
            createTestTrackingPoint("ride1", 0, isNavigationPoint = true),
            createTestTrackingPoint("ride1", 1, isNavigationPoint = false),
            createTestTrackingPoint("ride1", 2, isNavigationPoint = true)
        )
        println("创建测试轨迹点列表: ${points.joinToString()}")
        
        rideTrackingPointDao.insertTrackingPoints(points)
        println("所有轨迹点已插入数据库")
        
        val navigationPoints = rideTrackingPointDao.getRideNavigationPointsFlow("ride1").first()
        println("从数据库加载的导航点列表: ${navigationPoints.joinToString()}")
        assertEquals(2, navigationPoints.size)
        assertTrue(navigationPoints.all { it.isNavigationPoint })
        println("=== 测试完成 ===")
    }

    private fun createTestTrackingPoint(
        recordId: String,
        index: Int,
        isNavigationPoint: Boolean = false
    ) = RideTrackingPointEntity(
        recordId = recordId,
        latitude = 39.9 + index * 0.1,
        longitude = 116.4 + index * 0.1,
        altitude = 50.0 + index * 10,
        speed = 20f + index * 5,
        timestamp = System.currentTimeMillis() + index * 1000,
        isNavigationPoint = isNavigationPoint,
        regionCode = "region1"
    )
} 