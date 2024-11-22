package com.cyclequest.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index
import com.cyclequest.domain.model.sport.RideType
import com.cyclequest.domain.model.sport.RideStatus

@Entity(
    tableName = "ride_record",
    foreignKeys = [
        ForeignKey(entity = UserEntity::class, parentColumns = ["id"], childColumns = ["userId"], onDelete = ForeignKey.CASCADE)
//        ForeignKey(entity = BicycleEntity::class, parentColumns = ["id"], childColumns = ["bicycleId"], onDelete = ForeignKey.CASCADE),
//        ForeignKey(entity = PlannedRouteEntity::class, parentColumns = ["_id"], childColumns = ["plannedRouteId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index(value = ["userId"])]
)
data class RideRecordEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val recordId: Long = 0,
    val userId: String,
    val bicycleId: String?,
    val type: RideType,
    val routeId: String?,
    val startTime: Long,
    val endTime: Long?,
    val distance: Float,
    val duration: Int,
    val avgSpeed: Float,
    val maxSpeed: Float,
    val calories: Int,
    val status: RideStatus
)