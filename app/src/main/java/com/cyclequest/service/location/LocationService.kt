package com.cyclequest.service.location

import android.content.Context
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import timber.log.Timber

@Singleton
class LocationService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private lateinit var locationClient: AMapLocationClient

    init {
        try {
            AMapLocationClient.updatePrivacyShow(context, true, true)
            AMapLocationClient.updatePrivacyAgree(context, true)
            locationClient = AMapLocationClient(context)
            val option = AMapLocationClientOption()
            option.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
            locationClient.setLocationOption(option)
        } catch (e: Exception) {
            Timber.e(e, "初始化定位服务失败")
        }
    }

    fun startLocation() {
        try {
            locationClient.startLocation()
        } catch (e: Exception) {
            Timber.e(e, "启动定位失败")
        }
    }

    fun stopLocation() {
        try {
            locationClient.stopLocation()
        } catch (e: Exception) {
            Timber.e(e, "停止定位失败")
        }
    }
}
