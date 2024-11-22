package com.cyclequest.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.cyclequest.data.local.entity.RideRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RideRecordDao {
    @Insert
    suspend fun insertRideRecord(rideRecord: RideRecordEntity)

//    @Update
//    suspend fun updateRideRecord(rideRecord: RideRecordEntity)
//
//    @Query("SELECT * FROM ride_record")
//    fun getAllRideRecordsFlow(): Flow<List<RideRecordEntity>>
//
//    @Query("SELECT * FROM ride_record WHERE _id = :recordId")
//    suspend fun getRideRecordById(recordId: Long): RideRecordEntity?

//    @Query("SELECT * FROM ride_record WHERE userId = :userId")
//    fun getRideRecordsByUserId(userId: String): Flow<List<RideRecordEntity>>

    @Query("SELECT distance FROM ride_record WHERE userId = :userId")
    fun getRidesDistanceByUserId(userId: String): Flow<List<Float>>

//    @Query("SELECT * FROM ride_record WHERE plannedRouteId = :plannedRouteId")
//    fun getRideRecordsByPlannedRouteId(plannedRouteId: String): Flow<List<RideRecordEntity>>
//
//    @Query("DELETE FROM ride_record WHERE _id = :recordId")
//    suspend fun deleteRideRecord(recordId: Long)
//
//    @Query("DELETE FROM ride_record")
//    suspend fun deleteAllRideRecords()
}