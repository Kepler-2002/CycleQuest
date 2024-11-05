package com.cyclequest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bicycle_status_logs")
data class BicycleStatusLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val bicycleId: String,
    val userId: String?,
    val action: BikeAction,
    val timestamp: Long,
    val batteryLevel: Int,
    val latitude: Double?,
    val longitude: Double?
)

enum class BikeAction {
    LOCK,
    UNLOCK,
    LIGHT_ON,
    LIGHT_OFF,
    LOCATE,
    CONNECTED,
    DISCONNECTED
} 