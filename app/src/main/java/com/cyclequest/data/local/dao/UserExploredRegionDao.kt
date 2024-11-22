package com.cyclequest.data.local.dao

import androidx.room.*
import com.cyclequest.data.local.entity.UserExploredRegion

@Dao
interface UserExploredRegionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userExploredRegion: UserExploredRegion)

    @Update
    suspend fun update(userExploredRegion: UserExploredRegion)

    @Delete
    suspend fun delete(userExploredRegion: UserExploredRegion)

    @Query("SELECT * FROM user_explored_region WHERE userId = :userId AND regionCode = :regionCode")
    suspend fun getUserExploredRegion(userId: String, regionCode: String): UserExploredRegion?

    @Query("SELECT * FROM user_explored_region WHERE userId = :userId")
    suspend fun getAllUserExploredRegions(userId: String): List<UserExploredRegion>
} 