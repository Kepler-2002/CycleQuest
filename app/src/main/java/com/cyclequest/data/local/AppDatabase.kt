package com.cyclequest.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cyclequest.data.local.dao.*
import com.cyclequest.data.local.entity.*
import com.cyclequest.data.local.DatabaseConverters

@Database(
    entities = [
        UserEntity::class,
        UserSettingsEntity::class,
        AchievementEntity::class,
        UserAchievementEntity::class,
        UserDisplayedAchievementEntity::class,
        PostEntity::class,
        PostImageEntity::class,
        PostTagEntity::class,
        PostLikeEntity::class,
        CommentEntity::class,
        CommentLikeEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(DatabaseConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun userSettingsDao(): UserSettingsDao
    abstract fun achievementDao(): AchievementDao
    abstract fun userAchievementDao(): UserAchievementDao
    abstract fun userDisplayedAchievementDao(): UserDisplayedAchievementDao
    abstract fun postDao(): PostDao
    abstract fun postImageDao(): PostImageDao
    abstract fun postTagDao(): PostTagDao
    abstract fun postLikeDao(): PostLikeDao
    abstract fun commentDao(): CommentDao
    abstract fun commentLikeDao(): CommentLikeDao
}


