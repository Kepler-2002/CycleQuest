package com.cyclequest.core.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import org.greenrobot.eventbus.EventBus
import timber.log.Timber
import com.cyclequest.core.AppInitializer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.Gson

@Module
@InstallIn(SingletonComponent::class)
object BasicLibraryModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Provides
    @Singleton
    fun provideEventBus(): EventBus {
        return EventBus.getDefault()
    }

    @Provides
    @Singleton
    fun provideTimberTree(application: Application): Timber.Tree {
        return if (application.packageName.endsWith(".debug")) {
            Timber.DebugTree()
        } else {
            object : Timber.Tree() {
                override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                    // 在这里实现发布版本的日志处理逻辑
                    // 例如，可以将重要日志发送到服务器或保存到本地文件
                }
            }
        }
    }

    @Provides
    @Singleton
    fun provideAppInitializer(application: Application, timberTree: Timber.Tree): AppInitializer {
        return AppInitializer(application, timberTree)
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.example.com/") // 替换为您的API基础URL
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()
    // 为其他 Basic Library 组件提供类似的方法
}
