package com.cyclequest.data.local.dao

import androidx.room.*
import com.cyclequest.data.local.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RegionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRegion(region: RegionEntity)

    @Query("SELECT * FROM regions WHERE regionCode = :regionCode")
    suspend fun getRegionByCode(regionCode: String): RegionEntity?

    @Query("SELECT * FROM regions WHERE parentCode = :parentCode")
    fun getChildRegionsFlow(parentCode: String): Flow<List<RegionEntity>>

    @Query("SELECT * FROM regions WHERE level = :level")
    fun getRegionsByLevelFlow(level: RegionLevel): Flow<List<RegionEntity>>
} 