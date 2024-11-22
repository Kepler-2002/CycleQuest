package com.cyclequest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "region")
data class Region(
    @PrimaryKey val regionCode: String,
    val name: String,
    val level: String,
    val parentCode: String?,
    val centerLatitude: Double,
    val centerLongitude: Double,
    val boundaryData: String,
    val description: String?
) 