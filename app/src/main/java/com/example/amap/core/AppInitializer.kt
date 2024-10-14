package com.cyclequest.core

import android.app.Application
import com.amap.api.maps2d.MapsInitializer
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppInitializer @Inject constructor(
    private val application: Application,
    private val timberTree: Timber.Tree
) {
    fun initialize() {
        initializeLogging()
        initializeMapSDK()
        initializeServices()
        configureGlobalSettings()
    }

    private fun initializeLogging() {
        Timber.plant(timberTree)
        Timber.d("Logging initialized")
    }

    private fun initializeMapSDK() {
        MapsInitializer.initialize(application)
//        MapsInitializer.updatePrivacyShow(application, true, true)
//        MapsInitializer.updatePrivacyAgree(application, true)
        Timber.d("Map SDK initialized")
    }

    private fun initializeServices() {
        // 初始化其他全局服务
        Timber.d("Services initialized")
    }

    private fun configureGlobalSettings() {
        // 配置全局设置
        Timber.d("Global settings configured")
    }
}
