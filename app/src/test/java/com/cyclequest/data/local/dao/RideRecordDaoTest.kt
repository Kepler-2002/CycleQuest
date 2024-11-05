package com.cyclequest.data.local.dao

import com.cyclequest.data.local.TestDatabase
import com.cyclequest.data.local.entity.RideRecordEntity
import com.cyclequest.data.local.entity.RideStatus
import com.cyclequest.data.local.entity.RideType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class RideRecordDaoTest : TestDatabase() {
    private val rideRecordDao by lazy { getDatabase().rideRecordDao() }

    @Test
    fun insertAndGetRideRecord() = runBlocking {
        println("=== 测试插入和获取骑行记录 ===")
        val record = createTestRideRecord()
        println("创建测试骑行记录: $record")
        rideRecordDao.insertRideRecord(record)
        println("骑行记录已插入数据库")
        
        val loadedRecord = rideRecordDao.getRideRecordById(record.recordId)
        println("从数据库加载骑行记录: $loadedRecord")
        assertEquals(record, loadedRecord)
        println("=== 测试完成 ===")
    }

    @Test
    fun getUserRideRecords() = runBlocking {
        println("=== 测试获取用户骑行记录 ===")
        val records = listOf(
            createTestRideRecord("ride1", "user1"),
            createTestRideRecord("ride2", "user1"),
            createTestRideRecord("ride3", "user2")
        )
        println("创建测试骑行记录列表: ${records.joinToString()}")
        
        records.forEach { rideRecordDao.insertRideRecord(it) }
        println("所有骑行记录已插入数据库")
        
        val user1Records = rideRecordDao.getUserRideRecordsFlow("user1").first()
        println("获取user1的骑行记录: ${user1Records.joinToString()}")
        assertEquals(2, user1Records.size)
        println("=== 测试完成 ===")
    }

    @Test
    fun updateRideStats() = runBlocking {
        println("=== 测试更新骑行统计 ===")
        val record = createTestRideRecord("ride1")
        println("创建测试骑行记录: $record")
        rideRecordDao.insertRideRecord(record)
        println("骑行记录已插入数据库")
        
        val endTime = System.currentTimeMillis()
        rideRecordDao.updateRideStats(
            recordId = "ride1",
            endTime = endTime,
            distance = 10.5f,
            duration = 3600,
            avgSpeed = 15.5f,
            maxSpeed = 25.0f,
            calories = 450
        )
        println("更新骑行统计数据")
        
        val updatedRecord = rideRecordDao.getRideRecordById("ride1")
        println("更新后的骑行记录: $updatedRecord")
        assertEquals(endTime, updatedRecord?.endTime)
        assertEquals(10.5f, updatedRecord?.distance)
        println("=== 测试完成 ===")
    }

    private fun createTestRideRecord(
        recordId: String = "test_ride",
        userId: String = "test_user"
    ) = RideRecordEntity(
        recordId = recordId,
        userId = userId,
        bicycleId = "test_bike",
        type = RideType.FREE,
        plannedRouteId = null,
        startTime = System.currentTimeMillis(),
        endTime = null,
        distance = 0f,
        duration = 0,
        avgSpeed = 0f,
        maxSpeed = 0f,
        calories = 0,
        status = RideStatus.ONGOING
    )
} 