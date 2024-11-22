package com.cyclequest.domain.repository

import com.cyclequest.data.local.dao.UserExploredRegionDao
import com.cyclequest.data.local.dao.UserAchievementDao
import com.cyclequest.data.local.entity.UserExploredRegion
import com.cyclequest.data.local.entity.UserAchievementEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserExploredRegionRepository @Inject constructor(
    private val userExploredRegionDao: UserExploredRegionDao,
    private val userAchievementDao: UserAchievementDao
) {
    suspend fun addUserExploredRegion(userExploredRegion: UserExploredRegion) {
        val existingRegion = userExploredRegionDao.getUserExploredRegion(
            userExploredRegion.userId,
            userExploredRegion.regionCode
        )
        if (existingRegion == null) {
            userExploredRegionDao.insert(userExploredRegion)
            checkAndUnlockAchievements(userExploredRegion.userId)
        }
    }

    suspend fun updateUserExploredRegion(userExploredRegion: UserExploredRegion) {
        userExploredRegionDao.update(userExploredRegion)
    }

    suspend fun deleteUserExploredRegion(userExploredRegion: UserExploredRegion) {
        userExploredRegionDao.delete(userExploredRegion)
    }

    suspend fun getUserExploredRegions(userId: String): List<UserExploredRegion> {
        return userExploredRegionDao.getAllUserExploredRegions(userId)
    }

    suspend fun checkAndUnlockAchievements(userId: String) {
        val exploredRegions = getUserExploredRegions(userId)

        if (exploredRegions.size >= 5) {
            unlockAchievement(userId, "explore_5_regions", userAchievementDao)
        }
        if (exploredRegions.size >= 10) {
            unlockAchievement(userId, "explore_10_regions", userAchievementDao)
        }
    }

    private suspend fun unlockAchievement(userId: String, achievementId: String, achievementDao: UserAchievementDao) {
        val existingAchievement = achievementDao.getUserAchievement(userId, achievementId)
        if (existingAchievement == null) {
            val newAchievement = UserAchievementEntity(
                userId = userId,
                achievementId = achievementId,
                unlockedAt = System.currentTimeMillis(),
                progress = 100.0
            )
            achievementDao.insertUserAchievement(newAchievement)
        }
    }
} 