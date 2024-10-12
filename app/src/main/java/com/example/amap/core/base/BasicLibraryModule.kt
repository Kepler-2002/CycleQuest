package com.example.amap.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import com.example.amap.core.utils.LogManager
import com.example.amap.core.utils.EventBus
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.eventbus.EventBus
import java.util.logging.LogManager

// 其他必要的导入

@Module
@InstallIn(SingletonComponent::class)
object BasicLibraryModule {

    @Provides
    @Singleton
    fun provideLogManager(): LogManager {
        return LogManager()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Provides
    @Singleton
    fun provideEventBus(): EventBus {
        return EventBus()
    }
    
    @Provides
    @Singleton
    fun provideLocationService(@ApplicationContext context: Context): LocationService {
        return LocationService(context)
    }


    // 为其他 Basic Library 组件提供类似的方法
}
