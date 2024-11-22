package com.cyclequest.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "planned_route",
    foreignKeys = [
        ForeignKey(entity = UserEntity::class, parentColumns = ["id"], childColumns = ["userId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index(value = ["userId"])]
)
data class PlannedRouteEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val _id: Long = 0,
    val userId: String,
    val startName: String,
    val startLatitude: Double,
    val startLongitude: Double,
    val endName: String,
    val endLatitude: Double,
    val endLongitude: Double,
    val distance: Int,
    val duration: Int,
    val routeData: String,
    val createdAt: Long,
    val isFavorite: Boolean
)