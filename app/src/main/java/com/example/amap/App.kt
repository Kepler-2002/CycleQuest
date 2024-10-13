package com.example.amap

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import com.example.amap.core.AppInitializer
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var appInitializer: AppInitializer

    override fun onCreate() {
        super.onCreate()
        appInitializer.initialize()
    }
}
