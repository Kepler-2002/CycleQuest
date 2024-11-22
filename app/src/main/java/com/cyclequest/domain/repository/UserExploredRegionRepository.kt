package com.cyclequest.domain.repository

import com.cyclequest.data.local.dao.UserExploredRegionDao
import com.cyclequest.data.local.entity.UserExploredRegion
import javax.inject.Inject
import javax.inject.Singleton
import android.util.Log

@Singleton
class UserExploredRegionRepository @Inject constructor(
    private val userExploredRegionDao: UserExploredRegionDao
) {
    suspend fun addUserExploredRegion(userExploredRegion: UserExploredRegion) {
        val existingRegion = userExploredRegionDao.getUserExploredRegion(
            userExploredRegion.userId,
            userExploredRegion.regionCode
        )
        if (existingRegion == null) {
            userExploredRegionDao.insert(userExploredRegion)
            Log.d("UserExploredRepo", "新增区域探索记录: userId=${userExploredRegion.userId}, regionCode=${userExploredRegion.regionCode}")
        }
    }

    suspend fun updateUserExploredRegion(userExploredRegion: UserExploredRegion) {
        userExploredRegionDao.update(userExploredRegion)
        Log.d("UserExploredRepo", "更新区域探索记录: userId=${userExploredRegion.userId}, regionCode=${userExploredRegion.regionCode}, count=${userExploredRegion.exploredCount}")
    }

    suspend fun deleteUserExploredRegion(userExploredRegion: UserExploredRegion) {
        userExploredRegionDao.delete(userExploredRegion)
    }

    suspend fun getUserExploredRegions(userId: String): List<UserExploredRegion> {
        val regions = userExploredRegionDao.getAllUserExploredRegions(userId)
        Log.d("UserExploredRepo", "获取用户探索区域: userId=$userId, count=${regions.size}")
        return regions
    }
} 