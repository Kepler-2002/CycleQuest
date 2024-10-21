package com.cyclequest.service.location

import android.content.Context
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton
import timber.log.Timber

/**
 * LocationService 提供了两种主要的位置服务模式：
 * 
 * 1. 单次位置获取：
 *    使用 getCurrentLocation 方法获取当前位置的一次性快照。
 *    适用于用户手动请求当前位置的场景。
 * 
 * 2. 持续位置更新：
 *    适用于导航或轨迹追踪等需要持续监控位置变化的场景。
 *    使用流程如下：
 *    a. 调用 startLocationUpdates() 开始位置更新
 *    b. 通过 currentLocation StateFlow 监听位置变化
 *    c. 如需调整更新频率，使用 setLocationInterval(interval: Long)
 *    d. 完成后调用 stopLocationUpdates() 停止位置更新
 * 
 * 注意：在使用完毕后，特别是在 ViewModel 的 onCleared() 方法中，
 * 应调用 destroy() 方法以释放资源。
 */
@Singleton
class LocationService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private lateinit var locationClient: AMapLocationClient
    private val _currentLocation = MutableStateFlow<AMapLocation?>(null)
    val currentLocation: StateFlow<AMapLocation?> = _currentLocation
    private var isTracking = false
    private lateinit var locationListener: AMapLocationListener

    init {
        try {
            AMapLocationClient.updatePrivacyShow(context, true, true)
            AMapLocationClient.updatePrivacyAgree(context, true)
            locationClient = AMapLocationClient(context)
            val option = AMapLocationClientOption()
            option.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
            locationClient.setLocationOption(option)
            locationListener = AMapLocationListener { location ->
                if (location != null) {
                    if (location.errorCode == 0) {
                        _currentLocation.value = location
                    } else {
                        Timber.e("定位失败: ${location.errorCode}, ${location.errorInfo}")
                    }
                }
            }
            locationClient.setLocationListener(locationListener)
        } catch (e: Exception) {
            Timber.e(e, "初始化定位服务失败")
        }
    }

    // 获取单次位置更新，适用于用户点击按钮查看当前位置的场景。
    fun getCurrentLocation(callback: (AMapLocation?) -> Unit) {
        Timber.d("LocationService: 开始获取当前位置")
        try {
            val originalListener = locationListener
            locationClient.setLocationListener { location ->
                Timber.d("LocationService: 收到位置更新 - $location")
                if (location != null) {
                    if (location.errorCode == 0) {
                        Timber.d("LocationService: 位置更新成功")
                        callback(location)
                    } else {
                        Timber.e("LocationService: 获取位置失败 - 错误码: ${location.errorCode}, 错误信息: ${location.errorInfo}")
                        callback(null)
                    }
                } else {
                    Timber.e("LocationService: 获取位置失败 - 位置为空")
                    callback(null)
                }
                locationClient.setLocationListener(originalListener)
                locationClient.stopLocation()
            }
            Timber.d("LocationService: 开始请求位置")
            locationClient.startLocation()
        } catch (e: Exception) {
            Timber.e(e, "LocationService: 获取当前位置时发生异常")
            callback(null)
        }
    }

    // 开始持续的位置更新
    fun startLocationUpdates() {
        if (!isTracking) {
            try {
                locationClient.startLocation()
                isTracking = true
            } catch (e: Exception) {
                Timber.e(e, "启动位置更新失败")
            }
        }
    }
    // 停止持续的位置更新
    fun stopLocationUpdates() {
        if (isTracking) {
            try {
                locationClient.stopLocation()
                isTracking = false
            } catch (e: Exception) {
                Timber.e(e, "停止位置更新失败")
            }
        }
    }
    // 设置位置更新的间隔
    fun setLocationInterval(newInterval: Long) {
        locationClient.setLocationOption(AMapLocationClientOption().apply {
            interval = newInterval
        })
    }

    // 销毁位置客户端，释放资源
    fun destroy() {
        stopLocationUpdates()
        locationClient.onDestroy()
    }
}
