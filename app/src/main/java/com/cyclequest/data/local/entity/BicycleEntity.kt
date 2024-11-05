package com.cyclequest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bicycles")
data class BicycleEntity(
    @PrimaryKey
    val bicycleId: String,
    val model: String,
    val bluetoothId: String,
    val status: BikeStatus,
    val isLocked: Boolean,
    val isLightOn: Boolean,
    val lastKnownLatitude: Double?,
    val lastKnownLongitude: Double?,
    val lastUpdateTime: Long
)

enum class BikeStatus {
    IN_USE,
    AVAILABLE,
    MAINTENANCE,
    DISCONNECTED
} 