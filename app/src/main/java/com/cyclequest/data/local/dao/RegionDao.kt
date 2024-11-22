package com.cyclequest.data.local.dao

import androidx.room.*
import com.cyclequest.data.local.entity.Region

@Dao
interface RegionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(region: Region)

    @Update
    suspend fun update(region: Region)

    @Delete
    suspend fun delete(region: Region)

    @Query("SELECT * FROM region WHERE regionCode = :regionCode")
    suspend fun getRegionByCode(regionCode: String): Region?

    @Query("SELECT * FROM region")
    suspend fun getAllRegions(): List<Region>
} 