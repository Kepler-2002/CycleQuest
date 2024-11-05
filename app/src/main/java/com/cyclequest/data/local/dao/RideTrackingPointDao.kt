package com.cyclequest.data.local.dao

import androidx.room.*
import com.cyclequest.data.local.entity.RideTrackingPointEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RideTrackingPointDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrackingPoint(point: RideTrackingPointEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrackingPoints(points: List<RideTrackingPointEntity>)

    @Query("SELECT * FROM ride_tracking_points WHERE recordId = :recordId ORDER BY timestamp ASC")
    fun getRideTrackingPointsFlow(recordId: String): Flow<List<RideTrackingPointEntity>>

    @Query("SELECT * FROM ride_tracking_points WHERE recordId = :recordId AND isNavigationPoint = 1 ORDER BY timestamp ASC")
    fun getRideNavigationPointsFlow(recordId: String): Flow<List<RideTrackingPointEntity>>

    @Query("DELETE FROM ride_tracking_points WHERE recordId = :recordId")
    suspend fun deleteRideTrackingPoints(recordId: String)
} 