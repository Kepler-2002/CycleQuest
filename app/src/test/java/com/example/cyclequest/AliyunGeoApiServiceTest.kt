package com.cyclequest.service.network

import com.cyclequest.core.network.ApiServiceImpl
import com.cyclequest.service.aliyun.AliyunGeoApiService
import com.cyclequest.service.aliyun.AliyunGeoConfig
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class AliyunGeoApiServiceTest {

    private lateinit var apiService: ApiServiceImpl
    private lateinit var aliyunGeoConfig: AliyunGeoConfig
    private lateinit var aliyunGeoApiService: AliyunGeoApiService

    @Before
    fun setup() {
        val okHttpClient = OkHttpClient.Builder().build()
        apiService = ApiServiceImpl(okHttpClient)
        aliyunGeoConfig = AliyunGeoConfig()
        aliyunGeoApiService = AliyunGeoApiService(apiService, aliyunGeoConfig)
    }

    @Test
    fun `getAreaBoundary should return valid response for non-GeoJSON`() = runBlocking {
        val areaCode = "810015"
        val result = aliyunGeoApiService.getAreaBoundary(areaCode)

        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
        println("Non-GeoJSON Response: ${result.getOrNull()}")
    }

    @Test
    fun `getAreaBoundary should return valid response for GeoJSON`() = runBlocking {
        val areaCode = "810015"
        val result = aliyunGeoApiService.getAreaBoundary(areaCode, useGeoJson = true)

        assertTrue(result.isSuccess)
        assertNotNull(result.getOrNull())
        println("GeoJSON Response: ${result.getOrNull()}")
    }
}
