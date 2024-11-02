package com.cyclequest.domain.repository

import com.cyclequest.service.aliyun.AliyunGeoApiService
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class AdministrativeDivisionRepositoryTest {
    private lateinit var repository: AdministrativeDivisionRepository
    private lateinit var aliyunGeoApiService: AliyunGeoApiService

    @Before
    fun setup() {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://geo.datav.aliyun.com/")
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        aliyunGeoApiService = AliyunGeoApiService(retrofit)
        repository = AdministrativeDivisionRepository(aliyunGeoApiService, Gson())
    }

    @Test
    fun `test getAdministrativeDivisionBoundary with real API`() = runBlocking {
        // 测试香港湾仔区的边界
        val result = repository.getAdministrativeDivisionBoundary("810015", true)
        
        println("API Test Result:")
        result.fold(
            onSuccess = { division ->
                println("Success - Total Boundary Points: ${division.boundaryPoints.size}")
                println("First 3 points:")
                division.boundaryPoints.take(3).forEach { point ->
                    println("LatLng(${point.latitude}, ${point.longitude})")
                }
            },
            onFailure = { error ->
                println("Error: ${error.message}")
                error.printStackTrace()
            }
        )
    }

    @Test
    fun `test getAdministrativeDivisionBoundary with invalid code`() = runBlocking {
        val result = repository.getAdministrativeDivisionBoundary("invalid_code", true)
        
        println("Invalid Code Test Result:")
        result.fold(
            onSuccess = { division ->
                println("Unexpected success: $division")
            },
            onFailure = { error ->
                println("Error: ${error.message}")
                error.printStackTrace()
            }
        )
    }
} 