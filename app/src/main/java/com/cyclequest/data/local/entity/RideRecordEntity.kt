package com.cyclequest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ride_records")
data class RideRecordEntity(
    @PrimaryKey
    val recordId: String,
    val userId: String,
    val bicycleId: String?,
    val type: RideType,
    val plannedRouteId: String?,
    val startTime: Long,
    val endTime: Long?,
    val distance: Float,
    val duration: Int,
    val avgSpeed: Float,
    val maxSpeed: Float,
    val calories: Int,
    val status: RideStatus
)

enum class RideType {
    NAVIGATION,
    FREE
}

enum class RideStatus {
    ONGOING,
    COMPLETED,
    PAUSED,
    INTERRUPTED
} 