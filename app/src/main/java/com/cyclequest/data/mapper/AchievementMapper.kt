package com.cyclequest.data.mapper

import com.cyclequest.data.local.entity.AchievementEntity
import com.cyclequest.data.local.entity.UserAchievementEntity
import com.cyclequest.domain.model.Achievement
import com.cyclequest.domain.model.UserAchievement
import javax.inject.Inject

class AchievementMapper @Inject constructor() {
    
    fun toDomain(entity: AchievementEntity): Achievement {
        return Achievement(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            type = entity.type,
            requirement = entity.requirement,
            iconUrl = entity.iconUrl,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }

    fun toLocal(domain: Achievement): AchievementEntity {
        return AchievementEntity(
            id = domain.id,
            name = domain.name,
            description = domain.description,
            type = domain.type,
            requirement = domain.requirement,
            iconUrl = domain.iconUrl,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt
        )
    }

    fun toDomain(entity: UserAchievementEntity): UserAchievement {
        return UserAchievement(
            id = entity.id,
            userId = entity.userId,
            achievementId = entity.achievementId,
            unlockedAt = entity.unlockedAt,
            progress = entity.progress,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }

    fun toLocal(domain: UserAchievement): UserAchievementEntity {
        return UserAchievementEntity(
            id = domain.id,
            userId = domain.userId,
            achievementId = domain.achievementId,
            unlockedAt = domain.unlockedAt,
            progress = domain.progress,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt
        )
    }
} 