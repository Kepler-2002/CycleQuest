package com.cyclequest.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index
import androidx.room.ColumnInfo
import com.cyclequest.core.base.BaseEntity


@Entity(
    tableName = "user_achievements",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = AchievementEntity::class,
            parentColumns = ["id"],
            childColumns = ["achievementId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["userId"]),
        Index(value = ["achievementId"])
    ]
)
data class UserAchievementEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: String,
    val achievementId: String,
    val unlockedAt: Long,
    val progress: Double,
    @ColumnInfo(name = "created_at")
    override val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at")
    override val updatedAt: Long = System.currentTimeMillis()
) : BaseEntity