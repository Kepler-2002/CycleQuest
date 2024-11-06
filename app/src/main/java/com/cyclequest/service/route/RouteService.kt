package com.cyclequest.service.route

import android.content.Context
import android.util.Log
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.model.LatLng
import com.amap.api.maps2d.model.MarkerOptions
import com.amap.api.maps2d.model.BitmapDescriptorFactory
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.route.*
import com.cyclequest.R
import com.cyclequest.domain.model.AdministrativeDivision
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 路线规划服务类
 * 提供步行路线规划功能，使用高德地图SDK
 */
@Singleton
class RouteService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    // 高德地图路线搜索对象
    private val routeSearch: RouteSearch = RouteSearch(context)
    
    // 步行路线结果状态流
    private val _walkRouteResult = MutableStateFlow<WalkRouteResult?>(null)
    val walkRouteResult: StateFlow<WalkRouteResult?> = _walkRouteResult.asStateFlow()

    init {
        // 设置路线搜索监听器
        routeSearch.setRouteSearchListener(object : RouteSearch.OnRouteSearchListener {
            override fun onWalkRouteSearched(result: WalkRouteResult?, errorCode: Int) {
                if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                    if (result != null && result.paths != null) {
                        if (result.paths.size > 0) {
                            _walkRouteResult.value = result
                            Log.i("RouteService", "步行路线规划成功")
                        } else {
                            Log.e("RouteService", "步行路线规划失败：没有找到路线")
                        }
                    } else {
                        Log.e("RouteService", "步行路线规划失败：结果为空")
                    }
                } else {
                    Log.e("RouteService", "步行路线规划失败：错误码 $errorCode")
                }
            }

            override fun onRideRouteSearched(result: RideRouteResult?, errorCode: Int) {}
            override fun onBusRouteSearched(result: BusRouteResult?, errorCode: Int) {}
            override fun onDriveRouteSearched(result: DriveRouteResult?, errorCode: Int) {}
        })
    }

    /**
     * 搜索步行路线
     * @param startPoint 起点坐标
     * @param endPoint 终点坐标
     * @param mode 路线规划模式，默认为 RouteSearch.WALK_DEFAULT
     */
    fun searchWalkRoute(
        startPoint: LatLng = LatLng(39.909187, 116.397451),
        endPoint: LatLng = LatLng(39.914759, 116.408333),
        mode: Int = RouteSearch.WALK_DEFAULT
    ) {
        try {
            // 创建起终点对象
            val fromAndTo = RouteSearch.FromAndTo(
                LatLonPoint(startPoint.latitude, startPoint.longitude),
                LatLonPoint(endPoint.latitude, endPoint.longitude)
            )
            val boundaryPoints = mutableListOf<LatLng>()

            // 创建步行路线查询对象
            val query = RouteSearch.WalkRouteQuery(fromAndTo, mode)
            // 异步计算步行路线
            routeSearch.calculateWalkRouteAsyn(query)
            
            Log.i("RouteService", "Start routing")
//            return AdministrativeDivision(boundaryPoints)
        } catch (e: AMapException) {
            Log.e("RouteService", "routing Err:${e.message}")
        }
    }

    companion object {
        const val ROUTE_TYPE_WALK = 3
    }
}