package com.cyclequest.domain.usecase

import android.util.Log
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
        Log.d("AchievementDetector", "检查成就: userId=$userId, 已探索区域数=$exploredRegions")

        // 获取所有成就
        achievementRepository.getAllAchievements()
            .first()
            .filter { it.type == AchievementType.REGION_EXPLORER }
            .forEach { achievement ->
                // 检查是否已经解锁
                val isUnlocked = achievementRepository.isAchievementUnlocked(userId, achievement.id)
//                Log.d("AchievementDetector", "检查成就: ${achievement.name}, 要求=${achievement.requirement}, 已解锁=$isUnlocked")
                
                if (!isUnlocked && exploredRegions >= achievement.requirement) {
                    achievementRepository.unlockAchievement(userId, achievement.id)
                    newlyUnlockedAchievements.add(achievement)
                    Log.d("AchievementDetector", "解锁新成就: ${achievement.name}")
                }
            }

        return newlyUnlockedAchievements
    }
}