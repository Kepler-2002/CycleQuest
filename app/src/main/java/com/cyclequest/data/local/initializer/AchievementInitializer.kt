package com.cyclequest.data.local.initializer

import android.util.Log
import com.cyclequest.R
import com.cyclequest.data.local.dao.AchievementDao
import com.cyclequest.data.local.entity.AchievementEntity
import com.cyclequest.data.local.entity.AchievementType
import javax.inject.Inject

class AchievementInitializer @Inject constructor(
    private val achievementDao: AchievementDao
) {
    suspend fun initializeAchievements() {
        Log.d(TAG, "开始初始化成就系统")
        val existingAchievements = achievementDao.getAllAchievements()
        if (existingAchievements.isEmpty()) {
            Log.d(TAG, "数据库中无成就数据,开始插入默认成就")
            val achievements = getDefaultAchievements()
            achievementDao.insertAll(achievements)
            Log.d(TAG, "成功插入 ${achievements.size} 个默认成就")
        } else {
            Log.d(TAG, "数据库中已存在 ${existingAchievements.size} 个成就,跳过初始化")
        }
    }

    companion object {
        private const val TAG = "AchievementInitializer"
    }

    private fun getDefaultAchievements(): List<AchievementEntity> = listOf(
        // 累计骑行距离成就
        AchievementEntity(
            id = "total_distance_1",
            name = "初级骑手",
            description = "累计骑行距离达到10公里",
            type = AchievementType.TOTAL_DISTANCE,
            requirement = 10.0,
            resourceId = R.drawable.distance_bronze
        ),
        AchievementEntity(
            id = "total_distance_2",
            name = "中级骑手",
            description = "累计骑行距离达到50公里",
            type = AchievementType.TOTAL_DISTANCE,
            requirement = 50.0,
            resourceId = R.drawable.distance_silver
        ),
        AchievementEntity(
            id = "total_distance_3",
            name = "高级骑手",
            description = "累计骑行距离达到100公里",
            type = AchievementType.TOTAL_DISTANCE,
            requirement = 100.0,
            resourceId = R.drawable.distance_gold
        ),

//        // 累计骑行时间成就
//        AchievementEntity(
//            id = "total_time_1",
//            name = "骑行新手",
//            description = "累计骑行时间达到1小时",
//            type = AchievementType.TOTAL_TIME,
//            requirement = 3600.0, // 1小时(秒)
//            iconUrl = "achievements/time_bronze.png"
//        ),
//        AchievementEntity(
//            id = "total_time_2",
//            name = "骑行达人",
//            description = "累计骑行时间达到5小时",
//            type = AchievementType.TOTAL_TIME,
//            requirement = 18000.0, // 5小时(秒)
//            iconUrl = "achievements/time_silver.png"
//        ),
//
//        // 单次骑行距离成就
//        AchievementEntity(
//            id = "single_ride_1",
//            name = "短途专家",
//            description = "单次骑行距离超过5公里",
//            type = AchievementType.SINGLE_RIDE_DISTANCE,
//            requirement = 5.0,
//            iconUrl = "achievements/single_bronze.png"
//        ),
//        AchievementEntity(
//            id = "single_ride_2",
//            name = "长途王者",
//            description = "单次骑行距离超过20公里",
//            type = AchievementType.SINGLE_RIDE_DISTANCE,
//            requirement = 20.0,
//            iconUrl = "achievements/single_gold.png"
//        ),

        // 区域探索成就
        AchievementEntity(
            id = "region_explorer_1",
            name = "城市探索者",
            description = "探索3个不同的城市区域",
            type = AchievementType.REGION_EXPLORER,
            requirement = 3.0,
            resourceId = R.drawable.explorer_bronze
        ),
        AchievementEntity(
            id = "region_explorer_2",
            name = "探索大师",
            description = "探索10个不同的城市区域",
            type = AchievementType.REGION_EXPLORER,
            requirement = 10.0,
            resourceId = R.drawable.explorer_silver
        ),
        AchievementEntity(
            id = "region_explorer_3",
            name = "探索之神",
            description = "探索20个不同的城市区域",
            type = AchievementType.REGION_EXPLORER,
            requirement = 20.0,
            resourceId = R.drawable.explorer_gold
        )

    )
} 