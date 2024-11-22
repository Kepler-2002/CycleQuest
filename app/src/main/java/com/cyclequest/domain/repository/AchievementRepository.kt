package com.cyclequest.domain.repository

import com.cyclequest.data.local.dao.AchievementDao
import com.cyclequest.data.local.dao.UserAchievementDao
import com.cyclequest.data.local.dao.UserDisplayedAchievementDao
import com.cyclequest.data.local.entity.UserAchievementEntity
import com.cyclequest.data.local.entity.UserDisplayedAchievementEntity
import com.cyclequest.data.mapper.AchievementMapper
import com.cyclequest.domain.model.Achievement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AchievementRepository @Inject constructor(
    private val achievementDao: AchievementDao,
    private val userAchievementDao: UserAchievementDao,
    private val userDisplayedAchievementDao: UserDisplayedAchievementDao,
    private val achievementMapper: AchievementMapper
) {
    // 获取所有成就
    fun getAllAchievements(): Flow<List<Achievement>> {
        return achievementDao.getAllAchievementsFlow()
            .map { achievements -> achievements.map { achievementMapper.toDomain(it) } }
    }

    // 获取单个成就
    suspend fun getAchievementById(id: String): Achievement? {
        return achievementDao.getAchievementById(id)?.let { achievementMapper.toDomain(it) }
    }

    // 获取用户的所有成就
    fun getUserAchievements(userId: String): Flow<List<Achievement>> {
        return userAchievementDao.getUserAchievementsFlow(userId)
            .map { userAchievements ->
                userAchievements.mapNotNull { userAchievement ->
                    achievementDao.getAchievementById(userAchievement.achievementId)
                        ?.let { achievementMapper.toDomain(it) }
                }
            }
    }

    // 更新用户成就进度
    suspend fun updateUserProgress(userId: String, achievementId: String, progress: Double) {
        val userAchievement = userAchievementDao.getUserAchievement(userId, achievementId)
        if (userAchievement != null) {
            userAchievementDao.updateUserAchievement(userAchievement.copy(progress = progress))
        } else {
            userAchievementDao.insertUserAchievement(
                UserAchievementEntity(
                    userId = userId,
                    achievementId = achievementId,
                    progress = progress,
                    unlockedAt = if (progress >= 100.0) System.currentTimeMillis() else 0
                )
            )
        }
    }

    // 解锁成就
    suspend fun unlockAchievement(userId: String, achievementId: String) {
        val userAchievement = userAchievementDao.getUserAchievement(userId, achievementId)
        if (userAchievement != null) {
            userAchievementDao.updateUserAchievement(
                userAchievement.copy(
                    progress = 100.0,
                    unlockedAt = System.currentTimeMillis()
                )
            )
        } else {
            userAchievementDao.insertUserAchievement(
                UserAchievementEntity(
                    userId = userId,
                    achievementId = achievementId,
                    progress = 100.0,
                    unlockedAt = System.currentTimeMillis()
                )
            )
        }
    }

    // 设置用户展示的成就
    suspend fun setDisplayedAchievements(userId: String, achievementIds: List<String>) {
        userDisplayedAchievementDao.deleteAllUserDisplayedAchievements(userId)
        achievementIds.forEachIndexed { index, achievementId ->
            userDisplayedAchievementDao.insertUserDisplayedAchievement(
                UserDisplayedAchievementEntity(
                    userId = userId,
                    achievementId = achievementId,
                    displayOrder = index
                )
            )
        }
    }

    // 获取用户展示的成就
    fun getUserDisplayedAchievements(userId: String): Flow<List<Achievement>> {
        return userDisplayedAchievementDao.getUserDisplayedAchievementsFlow(userId)
            .map { displayedAchievements ->
                displayedAchievements.mapNotNull { displayed ->
                    achievementDao.getAchievementById(displayed.achievementId)
                        ?.let { achievementMapper.toDomain(it) }
                }
            }
    }
} 