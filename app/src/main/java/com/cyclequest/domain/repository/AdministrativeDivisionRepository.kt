package com.cyclequest.domain.repository

import com.cyclequest.service.aliyun.AliyunGeoApiService
import com.cyclequest.domain.model.AdministrativeDivision
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.amap.api.maps2d.model.LatLng
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdministrativeDivisionRepository @Inject constructor(
    private val aliyunGeoApiService: AliyunGeoApiService,
    private val gson: Gson
) {
    suspend fun getAdministrativeDivisionBoundary(divisionCode: String, useGeoJson: Boolean = false): Result<AdministrativeDivision> {
        return aliyunGeoApiService.getAreaBoundary(divisionCode, useGeoJson).map { jsonString ->
            parseJsonToAdministrativeDivision(jsonString)
        }
    }

    private fun parseJsonToAdministrativeDivision(jsonString: String): AdministrativeDivision {
        val jsonObject = gson.fromJson(jsonString, JsonObject::class.java)
        val features = jsonObject.getAsJsonArray("features")
        val geometry = features[0].asJsonObject.getAsJsonObject("geometry")
        
        val boundaryPoints = mutableListOf<LatLng>()
        
        // 处理 MultiPolygon 类型
        val polygons = geometry.getAsJsonArray("coordinates")
        // 获取第一个多边形（主要边界）
        val mainPolygon = polygons[0].asJsonArray
        // 获取多边形的外环
        val outerRing = mainPolygon[0].asJsonArray
        
        // 遍历所有坐标点
        for (i in 0 until outerRing.size()) {
            val point = outerRing[i].asJsonArray
            val lng = point[0].asDouble
            val lat = point[1].asDouble
            boundaryPoints.add(LatLng(lat, lng))
        }
        
        return AdministrativeDivision(boundaryPoints)
    }
}
