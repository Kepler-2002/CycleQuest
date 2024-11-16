package com.cyclequest.domain.repository

import com.cyclequest.service.aliyun.AliyunGeoApiService
import com.cyclequest.domain.model.AdministrativeDivision
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.amap.api.maps2d.model.LatLng
import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdministrativeDivisionRepository @Inject constructor(
    private val aliyunGeoApiService: AliyunGeoApiService,
    private val gson: Gson
) {
    suspend fun getAdministrativeDivisionBoundary(divisionCode: String, useGeoJson: Boolean = false): Result<AdministrativeDivision> {
        return try {
            Log.d("AdminDivisionRepo", "开始请求边界数据: $divisionCode")
            aliyunGeoApiService.getAreaBoundary(divisionCode, useGeoJson).map { jsonObject ->
                parseJsonToAdministrativeDivision(jsonObject)
            }
        } catch (e: Exception) {
            Log.e("AdminDivisionRepo", "获取边界数据失败", e)
            Result.failure(e)
        }
    }

    private fun parseJsonToAdministrativeDivision(jsonObject: JsonObject): AdministrativeDivision {
        try {
            Log.d("AdminDivisionRepo", "开始解析JSON对象")
            
            val features = jsonObject.getAsJsonArray("features")
                ?: throw IllegalStateException("features array is null")
            
            val geometry = features[0].asJsonObject.getAsJsonObject("geometry")

            val boundaryPoints = mutableListOf<LatLng>()
            
            // Polygon 结构: coordinates -> [外环] -> [坐标点]
            val coordinates = geometry.getAsJsonArray("coordinates")
                .get(0).asJsonArray  // 获取外环坐标点数组


            for (i in 0 until coordinates.size()) {
                val point = coordinates[i].asJsonArray
                val lng = point[0].asDouble
                val lat = point[1].asDouble
                boundaryPoints.add(LatLng(lat, lng))
            }
            
            Log.d("AdminDivisionRepo", "解析完成，边界点数量: ${boundaryPoints.size}")
            return AdministrativeDivision(boundaryPoints)
        } catch (e: Exception) {
            Log.e("AdminDivisionRepo", "解析JSON失败", e)
            throw e
        }
    }
}
