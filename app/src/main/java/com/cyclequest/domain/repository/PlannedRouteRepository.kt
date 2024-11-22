package com.cyclequest.domain.repository

import android.util.Log
import com.cyclequest.core.network.ApiError
import com.cyclequest.core.network.ApiResult
import com.cyclequest.data.local.dao.PlannedRouteDao
import com.cyclequest.data.local.entity.PlannedRouteEntity
import com.cyclequest.domain.model.PlannedPath
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.log

@Singleton
class PlannedRouteRepository @Inject constructor(
    private val plannedRouteDao: PlannedRouteDao
){
    fun list2D2String(list: List<List<Double>>): String {
        return list.joinToString(prefix = "[", postfix = "]", separator = ", ") { 
            "[${it[0]}, ${it[1]}]" 
        }
    }

    suspend fun saveRoute2DB(plannedPath: PlannedPath): ApiResult<PlannedPath> {
        Log.i("PRRepo", "saveRoute2DB")

        val pointLen = plannedPath.routeData.size

        val routePlanned = PlannedRouteEntity(
            userId = plannedPath.userId, // Add userId as a String
            startName = "", // Add startName as a String
            startLatitude = plannedPath.routeData[0][0], // Add startLatitude as a Double
            startLongitude = plannedPath.routeData[0][1], // Add startLongitude as a Double
            endName = "", // Add endName as a String
            endLatitude = plannedPath.routeData[pointLen-1][0], // Add endLatitude as a Double
            endLongitude = plannedPath.routeData[pointLen-1][1], // Add endLongitude as a Double
            distance = plannedPath.distance.toInt(), // Add distance as an Int
            duration = plannedPath.duration.toInt(), // Add duration as an Int
            routeData = list2D2String(plannedPath.routeData),
            createdAt = System.currentTimeMillis(), // Use current time as createdAt
            isFavorite = false // Set isFavorite as needed
        )

        try {
            plannedRouteDao.insertRoute(routePlanned)
            Log.i("PRRepo", "Insert ${routePlanned.routeData}")
            Log.i("PRRepo", "Insert Success")
            return ApiResult.Success(plannedPath)
        } catch (e: Exception) {
            Log.e("PRRepo", "Insert Error")
            return ApiResult.Error(ApiError.ServerError("Failed to register user: ${e.message}"))
        }
    }
}
