package com.cyclequest.core.di

import androidx.sqlite.db.SupportSQLiteDatabase
import com.cyclequest.core.database.sync.SyncConfig
import com.cyclequest.BuildConfig
import androidx.room.RoomDatabase
import com.cyclequest.core.database.DatabaseConfig
import com.cyclequest.core.database.sync.ConflictStrategy
import com.cyclequest.core.database.sync.RetryPolicy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.seconds
import timber.log.Timber
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.cyclequest.App
import com.cyclequest.data.local.AppDatabase
import com.cyclequest.data.local.dao.PostDao
import com.cyclequest.data.local.dao.UserDao
import com.cyclequest.data.local.dao.PlannedRouteDao
import com.cyclequest.data.local.dao.AchievementDao
import com.cyclequest.data.local.dao.PostImageDao
import com.cyclequest.data.local.dao.UserAchievementDao
import com.cyclequest.data.local.dao.UserDisplayedAchievementDao
import com.cyclequest.data.local.dao.UserExploredRegionDao
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    //在@Module中添加@provides方法，提供UserDao的实例，使得Hilt能为需要UserDao的地方提供实例
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase{
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "cyclequest_database"
        )
        .fallbackToDestructiveMigration()
        .addCallback(provideDatabaseCallback())
        .build()
    }
    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()    // 返回 UserDao实例
    }

    @Provides
    @Singleton
    fun provideUserExploredRegionDao(database: AppDatabase): UserExploredRegionDao {
        return database.userExploredRegionDao()    // Provide UserDisplayedAchievementDao instance
    }

    @Provides
    @Singleton
    fun providePostDao(database: AppDatabase): PostDao {
        return database.postDao()
    }

    @Provides
    @Singleton
    fun providePostImageDao(database: AppDatabase): PostImageDao {
        return database.postImageDao()
    }

    @Provides
    @Singleton
    fun providePlannedRouteDao(database: AppDatabase): PlannedRouteDao {
        return database.plannedRouteDao() // Provide PlannedRouteDao instance
    }

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideDatabaseConfig(): DatabaseConfig {
        return DatabaseConfig(
            name = "app.db",
            version = BuildConfig.DB_VERSION,
            enableWAL = true,
            enableForeignKeys = true
        )
    }

    @Provides
    @Singleton
    fun provideSyncConfig(): SyncConfig {
        return object : SyncConfig {
            override val syncInterval = 1.hours
            override val retryPolicy = RetryPolicy.Exponential(
                initialDelay = 5.seconds,
                maxAttempts = 3
            )
            override val conflictStrategy = ConflictStrategy.SERVER_WINS
        }
    }

    @Provides
    @Singleton
    fun provideDatabaseCallback(): RoomDatabase.Callback {
        return object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // 在这里执行数据库创建后的操作
                Timber.d("数据库已创建")
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                // 在这里执行数据库打开后的操作
                Timber.d("数据库已打开")
            }
        }
    }

    @Provides
    @Singleton
    fun provideAchievementDao(database: AppDatabase): AchievementDao {
        return database.achievementDao()
    }

    @Provides
    @Singleton
    fun provideUserAchievementDao(database: AppDatabase): UserAchievementDao {
        return database.userAchievementDao()
    }

    @Provides
    @Singleton
    fun provideUserDisplayedAchievementDao(database: AppDatabase): UserDisplayedAchievementDao {
        return database.userDisplayedAchievementDao()
    }
}
