package com.cyclequest.data.sync.Apis

import com.cyclequest.core.network.ApiResult
import com.cyclequest.data.local.entity.UserEntity
import com.cyclequest.service.backend.BackendService
import javax.inject.Inject


interface UserApi {
    suspend fun getUsers(): ApiResult<List<UserEntity>>
    suspend fun getUser(id: String): ApiResult<UserEntity>
    suspend fun createUser(user: UserEntity): ApiResult<UserEntity>
    suspend fun updateUser(user: UserEntity): ApiResult<UserEntity>
    suspend fun deleteUser(id: String): ApiResult<Unit>
}

class UserApiImpl @Inject constructor(
    private val backendService: BackendService
) : UserApi {
    override suspend fun getUsers(): ApiResult<List<UserEntity>> {
        return backendService.get("users")
    }

    override suspend fun getUser(id: String): ApiResult<UserEntity> {
        return backendService.get("users/$id")
    }

    override suspend fun createUser(user: UserEntity): ApiResult<UserEntity> {
        return backendService.post("users", user)
    }

    override suspend fun updateUser(user: UserEntity): ApiResult<UserEntity> {
        return backendService.put("users/${user.id}", user)
    }

    override suspend fun deleteUser(id: String): ApiResult<Unit> {
        return backendService.delete("users/$id")
    }
} 