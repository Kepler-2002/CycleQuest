package com.cyclequest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import com.cyclequest.core.base.BaseEntity

@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val type: AchievementType,
    val requirement: Double,
    val resourceId: Int, // 存储R.drawable.xxx的资源ID
    @ColumnInfo(name = "created_at")
    override val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at")
    override val updatedAt: Long = System.currentTimeMillis()
) : BaseEntity