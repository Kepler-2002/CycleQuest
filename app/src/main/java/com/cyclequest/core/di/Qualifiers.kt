// core/di/Qualifiers.kt
package com.cyclequest.core.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AliyunRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AmapRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BackendRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AppOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LoggingInterceptor

// 可能需要的其他限定符
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CacheDir

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DebugMode

