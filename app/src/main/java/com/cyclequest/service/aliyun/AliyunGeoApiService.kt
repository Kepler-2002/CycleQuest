package com.cyclequest.service.aliyun

import com.cyclequest.core.di.AliyunRetrofit
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AliyunGeoApiService @Inject constructor(
    @AliyunRetrofit private val retrofit: Retrofit
) {
    private val api = retrofit.create(AliyunGeoApi::class.java)

    suspend fun getAreaBoundary(areaCode: String, useGeoJson: Boolean = false): Result<String> {
        return try {
            val response = if (useGeoJson) {
                api.getAreaBoundaryGeoJson(areaCode)
            } else {
                api.getAreaBoundaryJson(areaCode)
            }

            if (response.isSuccessful) {
                Result.success(response.body() ?: throw IllegalStateException("Empty response"))
            } else {
                Result.failure(IllegalStateException("API error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
