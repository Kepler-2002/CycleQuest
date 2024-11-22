package com.cyclequest.domain.model

import com.cyclequest.data.local.entity.AchievementType

data class Achievement(
    val id: String,
    val name: String,
    val description: String,
    val type: AchievementType,
    val requirement: Double,
    val resourceId: Int,
    val createdAt: Long,
    val updatedAt: Long
) 