package com.cyclequest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "planned_routes")
data class PlannedRouteEntity(
    @PrimaryKey
    val routeId: String,
    val userId: String,
    val startName: String,
    val startLatitude: Double,
    val startLongitude: Double,
    val endName: String,
    val endLatitude: Double,
    val endLongitude: Double,
    val distance: Float,
    val duration: Int,
    val routeData: String,
    val createdAt: Long,
    val isFavorite: Boolean = false
) 