package com.cyclequest.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cyclequest.data.local.dao.UserDao
import com.cyclequest.data.local.entity.UserEntity


@Database(
    entities = [
        UserEntity::class
        // 其他实体
    ],
    version = 1
)
@TypeConverters(DatabaseConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}


