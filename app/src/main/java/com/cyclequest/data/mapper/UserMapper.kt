package com.cyclequest.data.mapper

import com.cyclequest.data.remote.UserDto
import com.cyclequest.domain.model.User

fun UserDto.toDomain() = User(
    id = id,
    name = name,
    age = age
)
