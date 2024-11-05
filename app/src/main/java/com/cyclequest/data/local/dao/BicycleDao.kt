package com.cyclequest.data.local.dao

import androidx.room.*
import com.cyclequest.data.local.entity.BicycleEntity
import com.cyclequest.data.local.entity.BikeStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface BicycleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBicycle(bicycle: BicycleEntity)

    @Query("SELECT * FROM bicycles WHERE bicycleId = :bicycleId")
    suspend fun getBicycleById(bicycleId: String): BicycleEntity?

    @Query("SELECT * FROM bicycles WHERE status = :status")
    fun getBicyclesByStatusFlow(status: BikeStatus): Flow<List<BicycleEntity>>

    @Query("UPDATE bicycles SET isLocked = :isLocked WHERE bicycleId = :bicycleId")
    suspend fun updateLockStatus(bicycleId: String, isLocked: Boolean)

    @Query("UPDATE bicycles SET isLightOn = :isLightOn WHERE bicycleId = :bicycleId")
    suspend fun updateLightStatus(bicycleId: String, isLightOn: Boolean)

    @Query("""
        UPDATE bicycles 
        SET lastKnownLatitude = :latitude,
            lastKnownLongitude = :longitude,
            lastUpdateTime = :timestamp
        WHERE bicycleId = :bicycleId
    """)
    suspend fun updateLocation(
        bicycleId: String,
        latitude: Double,
        longitude: Double,
        timestamp: Long
    )
} 