package com.cyclequest.core.initializer

import android.app.Application
import com.cyclequest.data.local.initializer.AchievementInitializer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppInitializer @Inject constructor(
    private val application: Application,
    private val loggingInitializer: LoggingInitializer,
    private val mapInitializer: MapInitializer,
    private val databaseInitializer: DatabaseInitializer,
    private val achievementInitializer: AchievementInitializer
) {
    fun initialize() {
        loggingInitializer.initialize()
        mapInitializer.initialize(application)
        databaseInitializer.initialize()
        // 在IO协程中初始化成就系统
        CoroutineScope(Dispatchers.IO).launch {
            achievementInitializer.initializeAchievements()
        }
    }
}
