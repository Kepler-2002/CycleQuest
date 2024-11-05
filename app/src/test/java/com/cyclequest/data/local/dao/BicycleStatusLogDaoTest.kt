package com.cyclequest.data.local.dao

import com.cyclequest.data.local.TestDatabase
import com.cyclequest.data.local.entity.BicycleStatusLogEntity
import com.cyclequest.data.local.entity.BikeAction
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class BicycleStatusLogDaoTest : TestDatabase() {
    private val bicycleStatusLogDao by lazy { getDatabase().bicycleStatusLogDao() }

    @Test
    fun insertAndGetStatusLogs() = runBlocking {
        println("=== 测试插入和获取自行车状态日志 ===")
        val logs = listOf(
            createTestStatusLog("bike1", BikeAction.LOCK),
            createTestStatusLog("bike1", BikeAction.UNLOCK),
            createTestStatusLog("bike1", BikeAction.LIGHT_ON)
        )
        println("创建测试日志列表: ${logs.joinToString()}")
        
        logs.forEach { 
            println("插入日志: $it")
            bicycleStatusLogDao.insertStatusLog(it)
        }
        println("所有日志已插入数据库")
        
        val loadedLogs = bicycleStatusLogDao.getBicycleStatusLogsFlow("bike1").first()
        println("从数据库加载的日志列表: ${loadedLogs.joinToString()}")
        assertEquals(3, loadedLogs.size)
        println("=== 测试完成 ===")
    }

    @Test
    fun getLastActionLog() = runBlocking {
        println("=== 测试获取最后一次操作日志 ===")
        val logs = listOf(
            createTestStatusLog("bike1", BikeAction.LOCK, System.currentTimeMillis() - 2000),
            createTestStatusLog("bike1", BikeAction.UNLOCK, System.currentTimeMillis() - 1000),
            createTestStatusLog("bike1", BikeAction.LOCK, System.currentTimeMillis())
        )
        println("创建测试日志列表: ${logs.joinToString()}")
        
        logs.forEach { bicycleStatusLogDao.insertStatusLog(it) }
        println("所有日志已插入数据库")
        
        val lastLockLog = bicycleStatusLogDao.getLastActionLog("bike1", BikeAction.LOCK)
        println("最后一次上锁日志: $lastLockLog")
        assertEquals(logs[2], lastLockLog?.copy(id = 0))
        println("=== 测试完成 ===")
    }

    @Test
    fun deleteOldLogs() = runBlocking {
        println("=== 测试删除旧日志 ===")
        val currentTime = System.currentTimeMillis()
        val logs = listOf(
            createTestStatusLog("bike1", BikeAction.LOCK, currentTime - 3000),
            createTestStatusLog("bike1", BikeAction.UNLOCK, currentTime - 2000),
            createTestStatusLog("bike1", BikeAction.LOCK, currentTime - 1000)
        )
        println("创建测试日志列表: ${logs.joinToString()}")
        
        logs.forEach { bicycleStatusLogDao.insertStatusLog(it) }
        println("所有日志已插入数据库")
        
        bicycleStatusLogDao.deleteOldLogs("bike1", currentTime - 1500)
        println("删除1.5秒前的日志")
        
        val remainingLogs = bicycleStatusLogDao.getBicycleStatusLogsFlow("bike1").first()
        println("剩余日志: ${remainingLogs.joinToString()}")
        assertEquals(1, remainingLogs.size)
        println("=== 测试完成 ===")
    }

    private fun createTestStatusLog(
        bicycleId: String,
        action: BikeAction,
        timestamp: Long = System.currentTimeMillis()
    ) = BicycleStatusLogEntity(
        bicycleId = bicycleId,
        userId = "test_user",
        action = action,
        timestamp = timestamp,
        batteryLevel = 80,
        latitude = 39.9,
        longitude = 116.4
    )
} 