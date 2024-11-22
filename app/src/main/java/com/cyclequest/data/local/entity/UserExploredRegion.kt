package com.cyclequest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_explored_region")
data class UserExploredRegion(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: String,
    val regionCode: String,
    val firstExploredTime: Long,
    val lastExploredTime: Long,
    val exploredCount: Int
) 