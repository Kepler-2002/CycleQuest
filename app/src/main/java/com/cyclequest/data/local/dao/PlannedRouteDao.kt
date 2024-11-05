package com.cyclequest.data.local.dao

import androidx.room.*
import com.cyclequest.data.local.entity.PlannedRouteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlannedRouteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlannedRoute(route: PlannedRouteEntity)

    @Query("SELECT * FROM planned_routes WHERE routeId = :routeId")
    suspend fun getPlannedRouteById(routeId: String): PlannedRouteEntity?

    @Query("SELECT * FROM planned_routes WHERE userId = :userId")
    fun getUserPlannedRoutesFlow(userId: String): Flow<List<PlannedRouteEntity>>

    @Query("SELECT * FROM planned_routes WHERE userId = :userId AND isFavorite = 1")
    fun getUserFavoriteRoutesFlow(userId: String): Flow<List<PlannedRouteEntity>>

    @Query("UPDATE planned_routes SET isFavorite = :isFavorite WHERE routeId = :routeId")
    suspend fun updateFavoriteStatus(routeId: String, isFavorite: Boolean)

    @Query("DELETE FROM planned_routes WHERE routeId = :routeId")
    suspend fun deletePlannedRoute(routeId: String)
} 