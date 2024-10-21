package com.cyclequest

import android.content.Context
import android.location.Location
import android.location.LocationManager
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.amap.api.location.AMapLocation
import com.cyclequest.service.location.LocationService
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import org.junit.Assert.*
import org.junit.After
import org.junit.Rule

@RunWith(AndroidJUnit4::class)
class LocationServiceIntegrationTest {

    private lateinit var locationService: LocationService

    @get:Rule
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)
    @Before
    fun setup() {
        println("开始设置测试环境")
        val context = ApplicationProvider.getApplicationContext<Context>()
        println("获取到应用上下文")
        locationService = LocationService(context)
        println("LocationService 初始化完成")

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        println("获取到 LocationManager")
        locationManager.addTestProvider(LocationManager.GPS_PROVIDER, false, false,
            false, false, false, false, false, 0, 0)
        println("添加测试 Provider")
        locationManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true)
        println("启用测试 Provider")

        val testLocation = Location(LocationManager.GPS_PROVIDER).apply {
            latitude = 40.7128 // 示例纬度
            longitude = -74.0060 // 示例经度
            accuracy = 3f
            time = System.currentTimeMillis()
        }
        println("创建测试位置")
        locationManager.setTestProviderLocation(LocationManager.GPS_PROVIDER, testLocation)
        println("设置测试位置完成")
    }

    @Test(timeout = 30000)
    fun testGetCurrentLocation() {
        println("开始测试获取当前位置")
        val latch = CountDownLatch(1)
        var result: AMapLocation? = null

        locationService.getCurrentLocation { location ->
            println("收到位置更新: $location")
            result = location
            latch.countDown()
        }

        println("等待位置更新")
        val success = latch.await(10, TimeUnit.SECONDS)
        println("等待结果: $success")
        assertTrue("取位置超时", success)
        assertNotNull("位置不应为空", result)
        println("获取到的位置: 经度=${result?.longitude}, 纬度=${result?.latitude}")
    }

    @Test
    fun testLocationUpdates() = runBlocking {
        val latch = CountDownLatch(1)
        var updatedLocation: AMapLocation? = null

        locationService.startLocationUpdates()

        val job = CoroutineScope(Dispatchers.Default).launch {
            locationService.currentLocation.collect { location ->
                if (location != null) {
                    updatedLocation = location
                    latch.countDown()
                }
            }
        }

        assertTrue("获取位置更新超时", latch.await(10, TimeUnit.SECONDS))
        assertNotNull("更新的位置不应为空", updatedLocation)
        println("更新的位置: 经度=${updatedLocation?.longitude}, 纬度=${updatedLocation?.latitude}")

        locationService.stopLocationUpdates()
        job.cancel()
    }

    @Test
    fun testSetLocationInterval() {
        val newInterval = 5000L // 5秒
        locationService.setLocationInterval(newInterval)
        // 这里我们无法直接验证间隔是否被正确设置，但至少可以确保方法调用不会抛出异
    }

    @After
    fun tearDown() {
        locationService.destroy()
    }
}
