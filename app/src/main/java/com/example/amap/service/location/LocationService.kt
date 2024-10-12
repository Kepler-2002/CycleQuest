package com.example.amap.service

import android.content.Context
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.example.amap.core.utils.PermissionUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private lateinit var locationClient: AMapLocationClient

    init {
        try {
            locationClient = AMapLocationClient(context)
            val option = AMapLocationClientOption()
            option.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
            locationClient.setLocationOption(option)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun startLocation() {
        if (PermissionUtil.isPermissionGranted(context, PermissionUtil.LOCATION)) {
            locationClient.startLocation()
        } else {
            // 处理权限未授予的情况
        }
    }

    fun stopLocation() {
        locationClient.stopLocation()
    }
}