//package com.cyclequest.service.route
//
//import android.Manifest
//import android.content.Context
//import androidx.test.core.app.ApplicationProvider
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import androidx.test.rule.GrantPermissionRule
//import com.amap.api.maps2d.model.LatLng
//import com.amap.api.services.route.WalkRouteResult
//import junit.framework.TestCase.assertNotNull
//import junit.framework.TestCase.assertTrue
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.flow.first
//import kotlinx.coroutines.runBlocking
//import kotlinx.coroutines.withTimeout
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//
//@OptIn(ExperimentalCoroutinesApi::class)
//@RunWith(AndroidJUnit4::class)
//class RouteServiceTest {
//
//    @get:Rule
//    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
//        Manifest.permission.ACCESS_FINE_LOCATION,
//        Manifest.permission.ACCESS_COARSE_LOCATION,
//        Manifest.permission.INTERNET,
//        Manifest.permission.ACCESS_NETWORK_STATE,
//        Manifest.permission.ACCESS_WIFI_STATE,
//        Manifest.permission.WRITE_EXTERNAL_STORAGE
//    )
//
//    private lateinit var routeService: RouteService
//    private lateinit var context: Context
//
//    @Before
//    fun setup() {
//        context = ApplicationProvider.getApplicationContext()
//        println("测试开始: 初始化 Context")
//
//        try {
//            routeService = RouteService(context)
//            println("RouteService 初始化成功")
//        } catch (e: Exception) {
//            println("RouteService 初始化失败: ${e.message}")
//            e.printStackTrace()
//        }
//    }
//
//    @Test
//    fun testRoutePlanningWithRealSDK() = runBlocking {
//        println("\n开始路线规划测试")
//
//        // 使用真实的北京坐标进行测试
//        val startPoint = LatLng(39.909187, 116.397451)  // 天安门
//        val endPoint = LatLng(39.914759, 116.408333)    // 故宫
//        println("起点: $startPoint (天安门)")
//        println("终点: $endPoint (故宫)")
//
//        try {
//            routeService.searchWalkRoute(startPoint, endPoint)
//            println("路线搜索请求已发送")
//
//            // 使用 withTimeout 来限制等待时间
//            val result = withTimeout(30_000) { // 30秒超时
//                var currentResult: WalkRouteResult? = null
//                var attempts = 0
//                val maxAttempts = 10
//
//                while (currentResult == null && attempts < maxAttempts) {
//                    currentResult = routeService.walkRouteResult.first()
//                    if (currentResult == null) {
//                        println("等待路线规划结果... 尝试次数: ${attempts + 1}")
//                        kotlinx.coroutines.delay(1000) // 每秒检查一次
//                    }
//                    attempts++
//                }
//                currentResult
//            }
//
//            println("获取到路线规划结果: ${result != null}")
//
//            if (result == null) {
//                println("路线规划结果为空")
//                println("可能的原因:")
//                println("1. API Key 未正确配置")
//                println("2. 网络连接问题")
//                println("3. 服务响应超时")
//                println("4. 回调未被正确触发")
//            } else {
//                println("路线数量: ${result.paths?.size}")
//                println("路线详情:")
//                result.paths?.forEachIndexed { index, path ->
//                    println("路线 ${index + 1}:")
//                    println("- 距离: ${path.distance}米")
//                    println("- 预计时间: ${path.duration}秒")
//                    println("- 步行路段数: ${path.steps?.size ?: 0}")
//                }
//            }
//
//            assertNotNull("路线规划结果不应为空", result)
//            assertTrue("应该找到至少一条路线", result?.paths?.isNotEmpty() == true)
//
//        } catch (e: Exception) {
//            println("测试过程中发生异常: ${e.message}")
//            println("异常堆栈:")
//            e.printStackTrace()
//            throw e
//        }
//    }
//}