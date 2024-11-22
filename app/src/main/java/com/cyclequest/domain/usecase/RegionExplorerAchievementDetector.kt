package com.cyclequest.domain.usecase

import com.cyclequest.data.local.entity.AchievementType
import com.cyclequest.domain.model.Achievement
import com.cyclequest.domain.repository.AchievementRepository
import com.cyclequest.domain.repository.UserExploredRegionRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegionExplorerAchievementDetector @Inject constructor(
    private val achievementRepository: AchievementRepository,
    private val userExploredRegionRepository: UserExploredRegionRepository
)  {
    suspend fun checkAchievements(userId: String): List<Achievement> {
        val newlyUnlockedAchievements = mutableListOf<Achievement>()
        val exploredRegions = userExploredRegionRepository.getUserExploredRegions(userId).size

        // 获取所有成就
        achievementRepository.getAllAchievements()
            .first()
            .filter { it.type == AchievementType.REGION_EXPLORER }
            .forEach { achievement ->
                // 检查是否已经解锁
                val isUnlocked = achievementRepository.isAchievementUnlocked(userId, achievement.id)
                if (!isUnlocked && exploredRegions >= achievement.requirement) {
                    achievementRepository.unlockAchievement(userId, achievement.id)
                    newlyUnlockedAchievements.add(achievement)
                }
            }

        return newlyUnlockedAchievements
    }
}