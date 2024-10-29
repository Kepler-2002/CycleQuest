package com.cyclequest.core.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.internal.bind.DateTypeAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.*
import javax.inject.Singleton

// core/di/JsonModule.kt
@Module
@InstallIn(SingletonComponent::class)
object JsonModule {
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .serializeNulls()  // 序列化null值
            .setPrettyPrinting()  // 美化输出
            .registerTypeAdapter(Date::class.java, DateTypeAdapter())  // 自定义类型适配器
            .create()
    }
}