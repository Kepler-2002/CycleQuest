package com.cyclequest.data.repository

import com.cyclequest.service.aliyun.AliyunGeoApiService
import com.cyclequest.data.model.AdministrativeDivision
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
        val coordinates = geometry.getAsJsonArray("coordinates")[0].asJsonArray

        val boundaryPoints = coordinates.map { coordinate ->
            val point = coordinate.asJsonArray
            LatLng(point[1].asDouble, point[0].asDouble)
        }

        return AdministrativeDivision(boundaryPoints)
    }
}
