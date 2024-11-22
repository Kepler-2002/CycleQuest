package com.cyclequest.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.cyclequest.data.local.entity.PlannedRouteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlannedRouteDao {
    @Insert
    suspend fun insertRoute(plannedRoute: PlannedRouteEntity)

    @Update
    suspend fun updateRoute(plannedRoute: PlannedRouteEntity)

    @Query("SELECT * FROM planned_route")
    fun getAllRoutesFlow(): Flow<List<PlannedRouteEntity>>

    @Query("SELECT * FROM planned_route WHERE _id = :routeId")
    suspend fun getRouteById(routeId: Long): PlannedRouteEntity?
  
    // 同步车控页面数据使用的查询方法
    @Query("SELECT * FROM planned_route WHERE userId = :userId ORDER BY createdAt DESC LIMIT 1")
    fun getLatestRouteFlow(userId: String): Flow<PlannedRouteEntity?>

    @Query("DELETE FROM planned_route WHERE _id = :routeId")
    suspend fun deleteRoute(routeId: Long)

    @Query("DELETE FROM planned_route")
    suspend fun deleteAllRoutes()

    @Query("SELECT * FROM planned_route WHERE userId = :userId")
    fun getRoutesByUserId(userId: String): Flow<List<PlannedRouteEntity>>
}