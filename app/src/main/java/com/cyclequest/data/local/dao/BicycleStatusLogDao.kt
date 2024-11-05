package com.cyclequest.data.local.dao

import androidx.room.*
import com.cyclequest.data.local.entity.BicycleStatusLogEntity
import com.cyclequest.data.local.entity.BikeAction
import kotlinx.coroutines.flow.Flow

@Dao
interface BicycleStatusLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStatusLog(log: BicycleStatusLogEntity)

    @Query("SELECT * FROM bicycle_status_logs WHERE bicycleId = :bicycleId ORDER BY timestamp DESC")
    fun getBicycleStatusLogsFlow(bicycleId: String): Flow<List<BicycleStatusLogEntity>>

    @Query("SELECT * FROM bicycle_status_logs WHERE bicycleId = :bicycleId AND action = :action ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastActionLog(bicycleId: String, action: BikeAction): BicycleStatusLogEntity?

    @Query("DELETE FROM bicycle_status_logs WHERE bicycleId = :bicycleId AND timestamp < :timestamp")
    suspend fun deleteOldLogs(bicycleId: String, timestamp: Long)
} 