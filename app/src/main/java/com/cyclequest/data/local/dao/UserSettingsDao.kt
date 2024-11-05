package com.cyclequest.data.local.dao

import androidx.room.*
import com.cyclequest.data.local.entity.UserSettingsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserSettingsDao {
    @Query("SELECT * FROM user_settings WHERE userId = :userId")
    fun getUserSettingsFlow(userId: String): Flow<UserSettingsEntity?>

    @Query("SELECT * FROM user_settings WHERE userId = :userId")
    suspend fun getUserSettings(userId: String): UserSettingsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserSettings(settings: UserSettingsEntity)

    @Update
    suspend fun updateUserSettings(settings: UserSettingsEntity)

    @Query("DELETE FROM user_settings WHERE userId = :userId")
    suspend fun deleteUserSettings(userId: String)
} 