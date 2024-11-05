package com.cyclequest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "regions")
data class RegionEntity(
    @PrimaryKey
    val regionCode: String,
    val name: String,
    val level: RegionLevel,
    val parentCode: String?,
    val centerLatitude: Double,
    val centerLongitude: Double,
    val boundaryData: String,
    val description: String?
)

enum class RegionLevel {
    CITY,
    DISTRICT
} 