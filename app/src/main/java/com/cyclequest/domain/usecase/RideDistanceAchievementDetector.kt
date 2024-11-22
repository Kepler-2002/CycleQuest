package com.cyclequest.domain.usecase

import android.util.Log
import com.cyclequest.data.local.entity.AchievementType
import com.cyclequest.domain.repository.AchievementRepository
import com.cyclequest.domain.repository.RideRecordRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RideDistanceAchievementDetector @Inject constructor(
    private val achievementRepository: AchievementRepository,
    private val rideRecordRepository: RideRecordRepository
) : AchievementDetector {
    override suspend fun checkAchievements(userId: String) {
        val totalDistance = rideRecordRepository.getUserTotalDistance(userId)
//        val totalDistance = 100

        Log.i("achievement", "$totalDistance")


        achievementRepository.getAllAchievements()
            .first()
            .filter { it.type == AchievementType.TOTAL_DISTANCE }
            .forEach { achievement ->
                val progress = (totalDistance / achievement.requirement) * 100
                if (progress >= 100.0) {
                    achievementRepository.unlockAchievement(userId, achievement.id)
                } else {
                    achievementRepository.updateUserProgress(userId, achievement.id, progress)
                }
            }
    }
}