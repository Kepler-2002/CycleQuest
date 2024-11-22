package com.cyclequest.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.cyclequest.data.local.entity.PlannedRouteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlannedRouteDao {
    @Insert
    suspend fun insertRoute(plannedRoute: PlannedRouteEntity)

//    @Update
//    suspend fun updateRoute(plannedRoute: PlannedRouteEntity)
//
//    @Query("SELECT * FROM planned_route")
//    fun getAllRoutesFlow(): Flow<List<PlannedRouteEntity>>
//
//    @Query("SELECT * FROM planned_route WHERE _id = :routeId")
//    suspend fun getRouteById(routeId: String): PlannedRouteEntity?
//
//    @Query("DELETE FROM planned_route WHERE _id = :routeId")
//    suspend fun deleteRoute(routeId: String)
//
//    @Query("DELETE FROM planned_route")
//    suspend fun deleteAllRoutes()
}