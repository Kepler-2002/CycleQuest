package com.cyclequest.core.di

import com.cyclequest.core.network.NetworkConfig
import com.cyclequest.service.aliyun.AliyunGeoApiService
import com.cyclequest.service.backend.BackendService
import com.cyclequest.data.sync.Apis.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import org.xml.sax.ErrorHandler
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

// core/di/NetworkModule.kt
@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    @BackendRetrofit
    fun provideBackendRetrofit(
        @AppOkHttpClient okHttpClient: OkHttpClient,
        networkConfig: NetworkConfig
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(networkConfig.baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

//    @Provides
//    @Singleton
//    fun provideBackendService(
//        @BackendRetrofit retrofit: Retrofit,
//        networkConfig: NetworkConfig,
//        errorHandler: ErrorHandler
//    ): BackendService {
//        return BackendService(retrofit, networkConfig, errorHandler)
//    }

    @Provides
    @Singleton
    fun provideAliyunGeoService(
        @AliyunRetrofit retrofit: Retrofit
    ): AliyunGeoApiService {
        return AliyunGeoApiService(retrofit)
    }

//    @Provides
//    @Singleton
//    fun provideUserApi(backendService: BackendService): UserApi {
//        return UserApi(backendService)
//    }

    @Provides
    @Singleton
    @AliyunRetrofit
    fun provideAliyunRetrofit(
        @AppOkHttpClient okHttpClient: OkHttpClient,
        networkConfig: NetworkConfig
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(networkConfig.aliyunBaseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}