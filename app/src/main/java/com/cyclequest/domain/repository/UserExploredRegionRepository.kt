package com.cyclequest.domain.repository

import com.cyclequest.data.local.dao.UserExploredRegionDao
import com.cyclequest.data.local.entity.UserExploredRegion
import javax.inject.Inject
import javax.inject.Singleton

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
} 