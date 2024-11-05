package com.cyclequest.data.local.dao

import com.cyclequest.data.local.TestDatabase
import com.cyclequest.data.local.entity.BicycleEntity
import com.cyclequest.data.local.entity.BikeStatus
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class BicycleDaoTest : TestDatabase() {
    private val bicycleDao by lazy { getDatabase().bicycleDao() }

    @Test
    fun insertAndGetBicycle() = runBlocking {
        println("=== 测试插入和获取自行车 ===")
        val bicycle = createTestBicycle()
        println("创建测试自行车: $bicycle")
        bicycleDao.insertBicycle(bicycle)
        println("自行车已插入数据库")
        
        val loadedBicycle = bicycleDao.getBicycleById(bicycle.bicycleId)
        println("从数据库加载自行车: $loadedBicycle")
        assertEquals(bicycle, loadedBicycle)
        println("=== 测试完成 ===")
    }

    @Test
    fun getBicyclesByStatus() = runBlocking {
        println("=== 测试按状态获取自行车 ===")
        val bicycles = listOf(
            createTestBicycle("bike1", BikeStatus.AVAILABLE),
            createTestBicycle("bike2", BikeStatus.IN_USE),
            createTestBicycle("bike3", BikeStatus.AVAILABLE)
        )
        println("创建测试自行车列表: ${bicycles.joinToString()}")
        
        bicycles.forEach { bicycleDao.insertBicycle(it) }
        println("所有自行车已插入数据库")
        
        val availableBikes = bicycleDao.getBicyclesByStatusFlow(BikeStatus.AVAILABLE).first()
        println("获取可用自行车: ${availableBikes.joinToString()}")
        assertEquals(2, availableBikes.size)
        println("=== 测试完成 ===")
    }

    @Test
    fun updateBicycleStatus() = runBlocking {
        println("=== 测试更新自行车状态 ===")
        val bicycle = createTestBicycle("bike1", BikeStatus.AVAILABLE)
        println("创建测试自行车: $bicycle")
        bicycleDao.insertBicycle(bicycle)
        println("自行车已插入数据库")
        
        bicycleDao.updateLockStatus("bike1", true)
        println("更新自行车锁定状态")
        
        val updatedBicycle = bicycleDao.getBicycleById("bike1")
        println("更新后的自行车: $updatedBicycle")
        assertEquals(true, updatedBicycle?.isLocked)
        println("=== 测试完成 ===")
    }

    private fun createTestBicycle(
        bicycleId: String = "test_bike",
        status: BikeStatus = BikeStatus.AVAILABLE
    ) = BicycleEntity(
        bicycleId = bicycleId,
        model = "Test Model",
        bluetoothId = "test_bluetooth",
        status = status,
        isLocked = false,
        isLightOn = false,
        lastKnownLatitude = 39.9,
        lastKnownLongitude = 116.4,
        lastUpdateTime = System.currentTimeMillis()
    )
} 