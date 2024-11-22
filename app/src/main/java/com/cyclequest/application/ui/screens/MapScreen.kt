package com.cyclequest.application.ui.screens

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import com.cyclequest.application.ui.components.map.MapPage
import com.cyclequest.application.viewmodels.MapViewModel
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.isGranted
import android.Manifest
import com.amap.api.maps2d.AMap
import com.cyclequest.application.ui.component.map.PillButton
import com.cyclequest.application.ui.component.map.RoutingLayer
import com.cyclequest.application.ui.component.map.DiscoveryLayer
import androidx.compose.foundation.interaction.MutableInteractionSource
import com.amap.api.maps2d.model.LatLng
import com.cyclequest.application.viewmodels.DiscoveryViewModel
import android.util.Log
import com.amap.api.maps2d.CameraUpdateFactory
import com.cyclequest.application.ui.components.map.SimulationMode
import com.cyclequest.application.viewmodels.RoutingViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    mapViewModel: MapViewModel = hiltViewModel(),
    routingViewModel: RoutingViewModel = hiltViewModel(),
    discoveryViewModel: DiscoveryViewModel = hiltViewModel()
) {
    val mapMode by mapViewModel.mapMode.collectAsState()
    var aMapInstance by remember { mutableStateOf<AMap?>(null) }


    // 权限处理
    val permissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    LaunchedEffect(Unit) {
        if (!permissionState.status.isGranted) {
            permissionState.launchPermissionRequest()
        }
    }

    // 计算当前的模拟模式
    val simulationMode = when {
        mapMode is MapViewModel.MapMode.Discovery && discoveryViewModel.isSimulationMode.value -> 
            SimulationMode.DISCOVERY
        mapMode is MapViewModel.MapMode.Routing && routingViewModel.isNavigationStarted.value -> 
            SimulationMode.NAVIGATION
        else -> SimulationMode.NONE
    }

    // 当地图模式改变时加载相应<数据>
    LaunchedEffect(mapMode) {
        when (mapMode) {
            // Discovery
            is MapViewModel.MapMode.Discovery -> {
                // 香港各区行政区划代码
                val hkDistrictCodes = listOf(
                    "810001", // 中西区
                    "810002", // 湾仔区
                    "810003", // 东区
                    "810004", // 南区
                    "810005", // 油尖旺区
                    "810006", // 深水埗区
                    "810007", // 九龙城区
                    "810008", // 黄大仙区
                    "810009", // 观塘区
                    "810010", // 荃湾区
                    "810011", // 屯门区
                    "810012", // 元朗区
                    "810013", // 北区
                    "810014", // 大埔区
                    "810015", // 西贡区
                    "810016", // 沙田区
                    "810017", // 葵青区
                    "810018"  // 离岛区
                )
                
                hkDistrictCodes.forEach { code ->
                    discoveryViewModel.loadBoundary(code)
                }
            }

            // Routing
            is MapViewModel.MapMode.Routing -> {
                // 获取当前位置坐标

                // 加载路线数据
//                routingViewModel.searchRoute(
//                    LatLng(22.3383,114.1720),
//                    LatLng(22.3361,114.1750)
//                )
            }
            else -> {}
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // 基础地图
        MapPage(
            modifier = Modifier.fillMaxSize(),
            onMapReady = { aMap ->
                Log.d("MapScreen", "地图初始化完成")
                aMapInstance = aMap
            },
            onLocationChanged = { location ->
                when (mapMode) {
                    is MapViewModel.MapMode.Discovery -> 
                        discoveryViewModel.locationUpdateCallback(location)
                    is MapViewModel.MapMode.Routing -> {}
                    else -> {}
                }
            },
            simulationMode = simulationMode,
            locationService = mapViewModel.locationService,
            navigationRoute = routingViewModel.routePoints.value
        )
        aMapInstance?.let { aMap ->
            // 根据模式显示不同图层
            when (mapMode) {
                is MapViewModel.MapMode.Discovery -> {
                    discoveryViewModel.provinceStates.forEach { (code, state) ->
                        val boundaryPoints = discoveryViewModel.provinceBoundaries[code] ?: emptyList()
                        DiscoveryLayer(
                            aMap = aMap,
                            boundaryPoints = boundaryPoints,
                            provinceState = state
                        )
                    }
                }

                    // <路线图层>
                    is MapViewModel.MapMode.Routing -> {
                        RoutingLayer(
                            aMap = aMap,
                            mapViewModel = mapViewModel,
                        )
                    }

                is MapViewModel.MapMode.Default -> {
                    // 默认地图模式，不需要额外图层
                }
            }

            // 顶部模式切换按钮
            PillButton(
                options = listOf("地图", "路线", "探索"),
                selectedOption = when (mapMode) {
                    is MapViewModel.MapMode.Default -> "地图"
                    is MapViewModel.MapMode.Routing -> "路线"
                    is MapViewModel.MapMode.Discovery -> "探索"
                },
                onOptionSelected = mapViewModel::setMapMode,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp)
            )

            // 右侧控制按钮
            Column(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                FloatingActionButton(
                    onClick = {
                        // 点击定位按钮时移动到当前位置
                        mapViewModel.locationService.getCurrentLocation { location ->
                            location?.let {
                                val currentLatLng = LatLng(it.latitude, it.longitude)
                                aMapInstance?.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17f))
                            }
                        }
                    },
                    modifier = Modifier.padding(bottom = 16.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    interactionSource = remember { MutableInteractionSource() },
                    content = {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "定位"
                        )
                    }
                )
            }

            // 只在探索模式下显示Demo按钮
            if (mapMode is MapViewModel.MapMode.Discovery) {
                val isSimulating by discoveryViewModel.isSimulationMode
                Button(
                    onClick = { discoveryViewModel.toggleSimulation() },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                ) {
                    Text(if (isSimulating) "停止模拟" else "开始模拟")
                }
            }
        }
    }
}

