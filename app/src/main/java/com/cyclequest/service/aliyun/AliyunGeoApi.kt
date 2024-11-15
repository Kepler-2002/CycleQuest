package com.cyclequest.service.aliyun

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AliyunGeoApi {
    @GET("areas_v3/bound/geojson")
    suspend fun getAreaBoundaryGeoJson(
        @Query("code") areaCode: String
    ): Response<JsonObject>

    @GET("areas_v3/bound/{code}.json")
    suspend fun getAreaBoundaryJson(
        @Path("code") areaCode: String
    ): Response<JsonObject>
}