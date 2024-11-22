package com.cyclequest.domain.usecase

    import com.cyclequest.data.local.entity.AchievementType
import com.cyclequest.domain.repository.AchievementRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegionExplorerAchievementDetector @Inject constructor(
    private val achievementRepository: AchievementRepository,
//    private val regionRepository: RegionRepository
) : AchievementDetector {
    override suspend fun checkAchievements(userId: String) {
//        val exploredRegions = regionRepository.getUserExploredRegions(userId).size
        val exploredRegions = 100


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
    }
}