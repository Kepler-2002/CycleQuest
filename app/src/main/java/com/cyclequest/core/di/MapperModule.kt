package com.cyclequest.core.di

import com.cyclequest.data.mapper.UserMapper
import com.cyclequest.data.mapper.AchievementMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


//创建 MapperModule 是必要的，因为它提供了 UserMapper 的依赖注入
@Module
@InstallIn(SingletonComponent::class)
object MapperModule {

    @Provides
    @Singleton
    fun provideUserMapper(): UserMapper {
        return UserMapper()
    }

    @Provides
    @Singleton
    fun provideAchievementMapper(): AchievementMapper {
        return AchievementMapper()
    }
}