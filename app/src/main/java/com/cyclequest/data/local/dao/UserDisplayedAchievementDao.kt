package com.cyclequest.data.local.dao

import androidx.room.*
import com.cyclequest.data.local.entity.UserDisplayedAchievementEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDisplayedAchievementDao {
    @Query("SELECT * FROM user_displayed_achievements WHERE userId = :userId ORDER BY displayOrder")
    fun getUserDisplayedAchievementsFlow(userId: String): Flow<List<UserDisplayedAchievementEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserDisplayedAchievement(achievement: UserDisplayedAchievementEntity)

    @Update
    suspend fun updateUserDisplayedAchievement(achievement: UserDisplayedAchievementEntity)

    @Query("DELETE FROM user_displayed_achievements WHERE userId = :userId AND achievementId = :achievementId")
    suspend fun deleteUserDisplayedAchievement(userId: String, achievementId: String)

    @Query("DELETE FROM user_displayed_achievements WHERE userId = :userId")
    suspend fun deleteAllUserDisplayedAchievements(userId: String)

    @Transaction
    suspend fun reorderUserDisplayedAchievements(achievements: List<UserDisplayedAchievementEntity>) {
        achievements.forEachIndexed { index, achievement ->
            updateUserDisplayedAchievement(achievement.copy(displayOrder = index))
        }
    }
} 