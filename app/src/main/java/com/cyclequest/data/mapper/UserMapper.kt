package com.cyclequest.data.mapper

import com.cyclequest.data.local.entity.UserEntity
import com.cyclequest.domain.model.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserMapper @Inject constructor() {
    fun toDomain(entity: UserEntity): User = User(
        id = entity.id,
        username = entity.username,
        email = entity.email,
        phoneNumber = entity.phoneNumber,
        password = entity.password,
        avatarUrl = entity.avatarUrl,
        status = entity.status,
        totalRides = entity.totalRides,
        totalDistance = entity.totalDistance,
        totalRideTime = entity.totalRideTime,
        lastLoginAt = entity.lastLoginAt,
        createdAt = entity.createdAt,
        updatedAt = entity.updatedAt
    )

    fun toLocal(domain: User): UserEntity = UserEntity(
        id = domain.id,
        username = domain.username,
        email = domain.email,
        phoneNumber = domain.phoneNumber,
        password = domain.password,
        avatarUrl = domain.avatarUrl,
        status = domain.status,
        totalRides = domain.totalRides,
        totalDistance = domain.totalDistance,
        totalRideTime = domain.totalRideTime,
        lastLoginAt = domain.lastLoginAt,
        createdAt = domain.createdAt,
        updatedAt = domain.updatedAt
    )
}
