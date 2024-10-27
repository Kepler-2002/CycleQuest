package com.cyclequest.service.backend

import retrofit2.Retrofit
import javax.inject.Inject


class BackendService @Inject constructor(
    private val retrofit: Retrofit
) {
    fun getApi(): Retrofit = retrofit

    suspend fun <T> request(call: suspend () -> T): T {
        return try {
            call()
        } catch (e: Exception) {
            // 统一错误处理
            throw e
        }
    }


}

