package com.cyclequest.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cyclequest.data.local.dao.UserDao
import com.cyclequest.data.local.dao.UserSettingsDao
import com.cyclequest.data.local.dao.AchievementDao
import com.cyclequest.data.local.dao.UserAchievementDao
import com.cyclequest.data.local.dao.UserDisplayedAchievementDao
import com.cyclequest.data.local.entity.UserEntity
import com.cyclequest.data.local.entity.UserSettingsEntity
import com.cyclequest.data.local.entity.AchievementEntity
import com.cyclequest.data.local.entity.UserAchievementEntity
import com.cyclequest.data.local.entity.UserDisplayedAchievementEntity


@Database(
    entities = [
        UserEntity::class,
        UserSettingsEntity::class,
        AchievementEntity::class,
        UserAchievementEntity::class,
        UserDisplayedAchievementEntity::class
    ],
    version = 1
)
@TypeConverters(DatabaseConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun userSettingsDao(): UserSettingsDao
    abstract fun achievementDao(): AchievementDao
    abstract fun userAchievementDao(): UserAchievementDao
    abstract fun userDisplayedAchievementDao(): UserDisplayedAchievementDao
}


