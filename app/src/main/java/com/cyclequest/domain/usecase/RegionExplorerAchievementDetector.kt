package com.cyclequest.domain.usecase

import com.cyclequest.data.local.entity.AchievementType
import com.cyclequest.domain.repository.AchievementRepository
import com.cyclequest.domain.repository.UserExploredRegionRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegionExplorerAchievementDetector @Inject constructor(
    private val achievementRepository: AchievementRepository,
    private val userExploredRegionRepository: UserExploredRegionRepository
) : AchievementDetector {
    override suspend fun checkAchievements(userId: String) {
        val exploredRegions = userExploredRegionRepository.getUserExploredRegions(userId).size

        achievementRepository.getAllAchievements()
            .first()
            .filter { it.type == AchievementType.REGION_EXPLORER }
            .forEach { achievement ->
                val progress = (exploredRegions / achievement.requirement) * 100
                if (progress >= 100.0) {
                    achievementRepository.unlockAchievement(userId, achievement.id)
                } else {
                    achievementRepository.updateUserProgress(userId, achievement.id, progress)
                }
            }

        // 检查特定的探索区域成就
        if (exploredRegions >= 5) {
            achievementRepository.unlockAchievement(userId, "explore_5_regions")
        }
        if (exploredRegions >= 10) {
            achievementRepository.unlockAchievement(userId, "explore_10_regions")
        }
    }
}