package com.cyclequest.domain.usecase

interface AchievementDetector {
    suspend fun checkAchievements(userId: String)
}