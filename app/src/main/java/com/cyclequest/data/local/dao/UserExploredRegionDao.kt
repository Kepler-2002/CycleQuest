package com.cyclequest.data.local.dao

import androidx.room.*
import com.cyclequest.data.local.entity.UserExploredRegionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserExploredRegionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserExploredRegion(userExploredRegion: UserExploredRegionEntity)

    @Query("SELECT * FROM user_explored_regions WHERE userId = :userId")
    fun getUserExploredRegionsFlow(userId: String): Flow<List<UserExploredRegionEntity>>

    @Query("SELECT * FROM user_explored_regions WHERE userId = :userId AND regionCode = :regionCode")
    suspend fun getUserExploredRegion(userId: String, regionCode: String): UserExploredRegionEntity?

    @Query("UPDATE user_explored_regions SET exploredCount = exploredCount + 1, lastExploredTime = :timestamp WHERE userId = :userId AND regionCode = :regionCode")
    suspend fun incrementExploredCount(userId: String, regionCode: String, timestamp: Long)
} 