package com.cyclequest.data.local.dao

import androidx.room.*
import com.cyclequest.data.local.entity.UserAchievementEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserAchievementDao {
    @Query("SELECT * FROM user_achievements WHERE userId = :userId")
    fun getUserAchievementsFlow(userId: String): Flow<List<UserAchievementEntity>>

    @Query("SELECT * FROM user_achievements WHERE userId = :userId AND achievementId = :achievementId")
    suspend fun getUserAchievement(userId: String, achievementId: String): UserAchievementEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserAchievement(userAchievement: UserAchievementEntity)

    @Update
    suspend fun updateUserAchievement(userAchievement: UserAchievementEntity)

    @Query("DELETE FROM user_achievements WHERE userId = :userId AND achievementId = :achievementId")
    suspend fun deleteUserAchievement(userId: String, achievementId: String)

    @Query("DELETE FROM user_achievements WHERE userId = :userId")
    suspend fun deleteAllUserAchievements(userId: String)
} 