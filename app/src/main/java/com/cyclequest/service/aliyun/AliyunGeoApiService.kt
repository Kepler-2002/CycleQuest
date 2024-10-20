package com.cyclequest.service.aliyun

import com.cyclequest.core.network.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AliyunGeoApiService @Inject constructor(
    private val apiService: ApiService,
    private val aliyunGeoConfig: AliyunGeoConfig
) {
    suspend fun getAreaBoundary(areaCode: String, useGeoJson: Boolean = false): Result<String> {
        val url = if (useGeoJson) {
            "${aliyunGeoConfig.baseUrl}areas_v3/bound/geojson?code=$areaCode"
        } else {
            "${aliyunGeoConfig.baseUrl}areas_v3/bound/$areaCode.json"
        }
        return apiService.get(url)
    }
}

data class AliyunGeoConfig(
    val baseUrl: String = "https://geo.datav.aliyun.com/"
)

