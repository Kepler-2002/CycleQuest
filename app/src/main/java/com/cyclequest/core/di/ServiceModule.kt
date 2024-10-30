package com.cyclequest.core.di

import com.cyclequest.service.aliyun.AliyunGeoApiService
import com.cyclequest.service.backend.BackendService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

// core/di/NetworkModule.kt
@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    @AliyunRetrofit
    fun provideAliyunRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://geo.datav.aliyun.com/")  // 阿里云 DataV GeoAPI 的基础 URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideBackendService(retrofit: Retrofit) = BackendService(retrofit)

    @Provides
    @Singleton
    fun provideAliyunGeoService(
        @AliyunRetrofit retrofit: Retrofit
    ): AliyunGeoApiService {
        return AliyunGeoApiService(retrofit)
    }


}