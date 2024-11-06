package com.cyclequest.service.route

import android.content.Context
import android.util.Log
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.model.LatLng
import com.amap.api.maps2d.model.MarkerOptions
import com.amap.api.maps2d.model.BitmapDescriptorFactory
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.core.ServiceSettings
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
    private lateinit var routeSearch: RouteSearch

    // 步行路线结果状态流
    private val _walkRouteResult = MutableStateFlow<WalkRouteResult?>(null)
    val walkRouteResult: StateFlow<WalkRouteResult?> = _walkRouteResult.asStateFlow()

    init {
        try {
            Log.i("RouteService", "开始初始化路由服务")
            ServiceSettings.updatePrivacyShow(context, true, true)  // 添加隐私政策说明参数
            ServiceSettings.updatePrivacyAgree(context, true)

            routeSearch = RouteSearch(context)
            println("RouteSearch 初始化完成")

            // 设置路线搜索监听器
            routeSearch.setRouteSearchListener(object : RouteSearch.OnRouteSearchListener {
                override fun onWalkRouteSearched(result: WalkRouteResult?, errorCode: Int) {
                    println("收到步行路线搜索结果, errorCode: $errorCode")
                    if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                        if (result != null && result.paths != null) {
                            if (result.paths.size > 0) {
                                _walkRouteResult.value = result
                                Log.i("RouteService", "步行路线规划成功")
                                println("步行路线规划成功: ${result.paths.size} 条路线")
                                result?.paths?.firstOrNull()?.steps?.forEach { step ->
                                    println("路段坐标点数量: ${step.polyline?.size}")
                                    step.polyline?.forEach { point ->
                                        println("坐标点: (${point.latitude}, ${point.longitude})")
                                    }
                                }
                                // 添加详细路线信息日志
//                                result.paths.forEachIndexed { index, path ->
//                                    println("路线 ${index + 1}:")
//                                    println("- 总距离: ${path.distance}米")
//                                    println("- 预计时间: ${path.duration}秒")
//                                    println("- 路段数量: ${path.steps?.size ?: 0}")
//
//                                    path.steps?.forEachIndexed { stepIndex, step ->
//                                        println("  步骤 ${stepIndex + 1}:")
//                                        println("  - 指示: ${step.instruction}")
//                                        println("  - 道路: ${step.road}")
//                                        println("  - 距离: ${step.distance}米")
//                                        println("  - 时间: ${step.duration}秒")
//                                        println("  - 方向: ${step.action}")
//                                    }
//                                }
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
            println("路线搜索监听器设置完成")
        } catch (e: Exception) {
            Log.e("RouteService", "初始化失败", e)
            throw e  // 让测试能够捕获到初始化失败
        }
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