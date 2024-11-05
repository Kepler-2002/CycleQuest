package com.cyclequest.domain.model

import com.cyclequest.data.local.entity.UserStatus

data class User(
    val id: String,
    val username: String,
    val email: String,
    val phoneNumber: String?,
    val password: String,
    val avatarUrl: String?,
    val status: UserStatus,
    val totalRides: Int,
    val totalDistance: Float,
    val totalRideTime: Long,
    val lastLoginAt: Long?,
    val createdAt: Long,
    val updatedAt: Long
)
