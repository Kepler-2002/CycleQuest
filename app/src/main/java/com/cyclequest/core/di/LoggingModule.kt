package com.cyclequest.core.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import timber.log.Timber
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object LoggingModule {
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


}