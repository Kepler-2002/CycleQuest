package com.cyclequest.domain.model

data class UserAchievement(
    val id: Long,
    val userId: String,
    val achievementId: String,
    val unlockedAt: Long,
    val progress: Double,
    val createdAt: Long,
    val updatedAt: Long
) 