package com.cyclequest.data.remote

data class UserDto(
    val id: Long,
    val name: String,
    val age: Int,
    val avatar: String?,
    val email: String?,
    val phone: String?,
    val createTime: Long,
    val updateTime: Long,
    val status: Int, // 0-正常 1-禁用
    val extra: Map<String, Any>? = null
)
