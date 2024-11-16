package com.cyclequest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val type: AchievementType,
    val requirement: Double,
    val iconUrl: String
) 