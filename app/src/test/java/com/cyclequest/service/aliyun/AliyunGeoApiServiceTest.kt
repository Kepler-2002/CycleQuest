package com.cyclequest.service.aliyun

import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class AliyunGeoApiServiceTest {
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
    }

    @Test
    fun `test getAreaBoundary with normal JSON`() = runBlocking {
        val areaCode = "810015"
        val result = aliyunGeoApiService.getAreaBoundary(areaCode)
        
        println("Normal JSON Response:")
        result.fold(
            onSuccess = { response -> println("Success: $response") },
            onFailure = { error -> println("Error: $error") }
        )
    }

    @Test
    fun `test getAreaBoundary with GeoJSON`() = runBlocking {
        val areaCode = "810015"
        val result = aliyunGeoApiService.getAreaBoundary(areaCode, useGeoJson = true)
        
        println("GeoJSON Response:")
        result.fold(
            onSuccess = { response -> println("Success: $response") },
            onFailure = { error -> println("Error: $error") }
        )
    }

    @Test
    fun `test getAreaBoundary with invalid code`() = runBlocking {
        val areaCode = "invalid_code"
        val result = aliyunGeoApiService.getAreaBoundary(areaCode)
        
        println("Invalid Code Response:")
        result.fold(
            onSuccess = { response -> println("Success: $response") },
            onFailure = { error -> println("Error: $error") }
        )
    }
} 