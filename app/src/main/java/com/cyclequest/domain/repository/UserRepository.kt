package com.cyclequest.domain.repository

import com.cyclequest.data.remote.UserApi
import com.cyclequest.data.remote.UserDto
import com.cyclequest.domain.model.User
import com.cyclequest.service.backend.BackendService
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val backendService: BackendService
){
    // 使用backendService创建API实例
    private val api = backendService.getApi().create(UserApi::class.java)

    suspend fun getUser(id: Long): User {  // 2. 返回 domain 模型而不是 DTO
        return backendService.request {
            api.getUser(id).toDomain()
        }
    }

}