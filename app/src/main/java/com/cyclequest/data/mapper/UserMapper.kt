package com.cyclequest.data.mapper

import com.cyclequest.data.remote.UserDto
import com.cyclequest.domain.model.User

object UserMapper {
    fun UserDto.toDomain() = User(
        id = id,
        name = name,
        age = age
    )

    fun User.toDto() = UserDto(
        id = id,
        name = name,
        age = age,
        avatar = null,
        email = null,
        phone = null,
        createTime = System.currentTimeMillis(),
        updateTime = System.currentTimeMillis(),
        status = 0,
        extra = null
    )

    fun List<UserDto>.toDomainList() = map { it.toDomain() }

    fun List<User>.toDtoList() = map { it.toDto() }
}
