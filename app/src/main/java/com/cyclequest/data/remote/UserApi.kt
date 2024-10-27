package com.cyclequest.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface UserApi {
    @GET("users/{id}")
    suspend fun getUser(@Path("id") id: Long): UserDto

    @GET("users")
    suspend fun getUsers(): List<UserDto>
}
