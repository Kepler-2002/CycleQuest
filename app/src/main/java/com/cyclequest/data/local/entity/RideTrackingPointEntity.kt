package com.cyclequest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ride_tracking_points")
data class RideTrackingPointEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val recordId: String,
    val latitude: Double,
    val longitude: Double,
    val altitude: Double?,
    val speed: Float,
    val timestamp: Long,
    val isNavigationPoint: Boolean,
    val regionCode: String?
) 