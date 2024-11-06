package com.cyclequest.service.route

import android.content.Context
import android.util.Log
import com.amap.api.maps2d.model.LatLng
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.core.ServiceSettings
import com.amap.api.services.route.*
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
    private lateinit var routeSearch: RouteSearch

    // 步行路线结果状态流
    private val _rideRouteResult = MutableStateFlow<RideRouteResult?>(null)
    val rideRouteResult: StateFlow<RideRouteResult?> = _rideRouteResult.asStateFlow()

    // 路线信息状态流
    private val _routeInfo = MutableStateFlow<RouteInfo?>(null)
    val routeInfo: StateFlow<RouteInfo?> = _routeInfo.asStateFlow()

    init {
        try {
            Log.i("RouteService", "开始初始化路由服务")
            ServiceSettings.updatePrivacyShow(context, true, true)  // 添加隐私政策说明参数
            ServiceSettings.updatePrivacyAgree(context, true)

            routeSearch = RouteSearch(context).apply {
                setRouteSearchListener(object : RouteSearch.OnRouteSearchListener {
                    override fun onRideRouteSearched(result: RideRouteResult?, errorCode: Int) {
                        println("收到骑行路线搜索结果, errorCode: $errorCode")
                        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                            handleRideRouteResult(result)
                        } else {
                            Log.e("RouteService", "骑行路线规划失败：错误码 $errorCode")
                        }
                    }
                    
                    // 其他回调保持空实现
                    override fun onWalkRouteSearched(result: WalkRouteResult?, errorCode: Int) {}
                    override fun onBusRouteSearched(result: BusRouteResult?, errorCode: Int) {}
                    override fun onDriveRouteSearched(result: DriveRouteResult?, errorCode: Int) {}
                })
            }
            println("路线搜索监听器设置完成")
        } catch (e: Exception) {
            Log.e("RouteService", "初始化失败", e)
            throw e  // 让测试能够捕获到初始化失败
        }
    }

    private fun handleRideRouteResult(result: RideRouteResult?) {
        if (result?.paths?.isNotEmpty() == true) {
            val path = result.paths.first()
            _rideRouteResult.value = result
            
            // 提取并发送路线详细信息
            _routeInfo.value = RouteInfo(
                totalDistance = path.distance,
                totalDuration = path.duration,
                steps = path.steps?.map { step ->
                    RouteStep(
                        instruction = step.instruction,
                        road = step.road ?: "",
                        distance = step.distance,
                        duration = step.duration
                    )
                } ?: emptyList()
            )
        }
    }

    suspend fun searchRideRoute(from: LatLng, to: LatLng) {
        val fromPoint = LatLonPoint(from.latitude, from.longitude)
        val toPoint = LatLonPoint(to.latitude, to.longitude)
        val fromAndTo = RouteSearch.FromAndTo(fromPoint, toPoint)
        val query = RouteSearch.RideRouteQuery(fromAndTo)
        routeSearch.calculateRideRouteAsyn(query)
    }

    data class RouteInfo(
        val totalDistance: Float,  // 总距离（米）
        val totalDuration: Long,  // 预计时间（秒）
        val steps: List<RouteStep>  // 导航步骤
    )

    data class RouteStep(
        val instruction: String,  // 导航指示
        val road: String,        // 道路名称
        val distance: Float,       // 该段距离
        val duration: Float        // 该段时间
    )
}