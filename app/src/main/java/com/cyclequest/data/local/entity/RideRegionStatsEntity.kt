package com.cyclequest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ride_region_stats")
data class RideRegionStatsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: String,
    val regionCode: String,
    val totalDistance: Float,
    val totalDuration: Int,
    val totalRides: Int,
    val lastUpdated: Long
) 