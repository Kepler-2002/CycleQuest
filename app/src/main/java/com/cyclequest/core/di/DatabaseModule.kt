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
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
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
}