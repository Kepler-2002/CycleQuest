package com.cyclequest.data.local.dao

import androidx.room.*
import com.cyclequest.data.local.entity.RideRegionStatsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RideRegionStatsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRideRegionStats(stats: RideRegionStatsEntity)

    @Query("SELECT * FROM ride_region_stats WHERE userId = :userId AND regionCode = :regionCode")
    fun getRideRegionStatsFlow(userId: String, regionCode: String): Flow<RideRegionStatsEntity?>

    @Query("SELECT * FROM ride_region_stats WHERE userId = :userId")
    fun getUserRideRegionStatsFlow(userId: String): Flow<List<RideRegionStatsEntity>>

    @Query("""
        UPDATE ride_region_stats 
        SET totalDistance = totalDistance + :distance,
            totalDuration = totalDuration + :duration,
            totalRides = totalRides + 1,
            lastUpdated = :timestamp
        WHERE userId = :userId AND regionCode = :regionCode
    """)
    suspend fun updateRideRegionStats(
        userId: String,
        regionCode: String,
        distance: Float,
        duration: Int,
        timestamp: Long
    )
} 