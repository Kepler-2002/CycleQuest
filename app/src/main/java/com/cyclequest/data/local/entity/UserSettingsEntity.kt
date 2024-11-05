package com.cyclequest.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "user_settings",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UserSettingsEntity(
    @PrimaryKey
    val userId: String,
    val isDarkMode: Boolean,
    val isNotificationEnabled: Boolean,
    val language: String,
    val showDistance: Boolean
) 