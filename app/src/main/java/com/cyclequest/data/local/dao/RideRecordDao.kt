package com.cyclequest.data.local.dao

import androidx.room.*
import com.cyclequest.data.local.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RideRecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRideRecord(record: RideRecordEntity)

    @Query("SELECT * FROM ride_records WHERE recordId = :recordId")
    suspend fun getRideRecordById(recordId: String): RideRecordEntity?

    @Query("SELECT * FROM ride_records WHERE userId = :userId ORDER BY startTime DESC")
    fun getUserRideRecordsFlow(userId: String): Flow<List<RideRecordEntity>>

    @Query("UPDATE ride_records SET status = :status WHERE recordId = :recordId")
    suspend fun updateRideStatus(recordId: String, status: RideStatus)

    @Query("UPDATE ride_records SET endTime = :endTime, distance = :distance, duration = :duration, avgSpeed = :avgSpeed, maxSpeed = :maxSpeed, calories = :calories WHERE recordId = :recordId")
    suspend fun updateRideStats(
        recordId: String,
        endTime: Long,
        distance: Float,
        duration: Int,
        avgSpeed: Float,
        maxSpeed: Float,
        calories: Int
    )
} 