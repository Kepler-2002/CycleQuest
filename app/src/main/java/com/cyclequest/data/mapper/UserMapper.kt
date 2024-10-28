package com.cyclequest.data.mapper

import com.cyclequest.data.local.entity.UserEntity
import com.cyclequest.domain.model.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserMapper @Inject constructor() {
    fun toDomain(entity: UserEntity): User = User(
        id = entity.id,
        name = entity.name,
        email = entity.email
    )

    fun toLocal(domain: User): UserEntity = UserEntity(
        id = domain.id,
        name = domain.name,
        email = domain.email
    )
}
