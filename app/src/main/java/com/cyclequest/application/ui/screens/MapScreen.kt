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
import com.cyclequest.application.ui.components.map.rememberCameraPositionState
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
import com.cyclequest.application.ui.component.map.RouteInfoPanel
import com.cyclequest.application.viewmodels.RoutingViewModel
import com.cyclequest.application.viewmodels.DiscoveryViewModel
import android.util.Log

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    mapViewModel: MapViewModel = hiltViewModel(),
    routingViewModel: RoutingViewModel = hiltViewModel(),
    discoveryViewModel: DiscoveryViewModel = hiltViewModel()
) {
    val mapMode by mapViewModel.mapMode.collectAsState()
    val currentLocation by mapViewModel.currentLocation.collectAsState()
    val routePoints by routingViewModel.routePoints.collectAsState()
    val routeInfo by routingViewModel.routeInfo.collectAsState()
    val boundaryPoints by discoveryViewModel.boundaryPoints.collectAsState()
    var aMapInstance by remember { mutableStateOf<AMap?>(null) }
    val cameraPositionState = rememberCameraPositionState()
    val isRouteInfoMinimized by routingViewModel.isRouteInfoMinimized.collectAsState()

    // 权限处理
    val permissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    LaunchedEffect(Unit) {
        if (!permissionState.status.isGranted) {
            permissionState.launchPermissionRequest()
        }
    }

    // 当地图模式改变时加载相应数据
    LaunchedEffect(mapMode) {
        when (mapMode) {
            is MapViewModel.MapMode.Discovery -> {
                discoveryViewModel.loadBoundary("810006")

            }
            is MapViewModel.MapMode.Routing -> {
                // 加载路线数据
                routingViewModel.searchRoute(
                    LatLng(39.909187, 116.397451),
                    LatLng(39.914759, 116.408333)
                )
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
                discoveryViewModel.locationUpdateCallback(location) }
        )
        aMapInstance?.let { aMap ->
            // 根据模式显示不同图层
            aMapInstance?.let { aMap ->
                when (mapMode) {
                    is MapViewModel.MapMode.Discovery -> {
                        DiscoveryLayer(
                            aMap = aMap,
                            boundaryPoints = boundaryPoints
                        )
                    }

                    is MapViewModel.MapMode.Routing -> {
                        RoutingLayer(
                            aMap = aMap,
                            routePoints = routePoints,
                        )
                    }

                    is MapViewModel.MapMode.Default -> {
                        // 默认地图模式，不需要额外图层
                    }
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
                    onClick = {},
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
        }

        // 只在路线模式下且有路线信息时显示面板
        if (mapMode is MapViewModel.MapMode.Routing && routePoints.isNotEmpty()) {
            routeInfo?.let { info ->
                Box(modifier = Modifier.fillMaxSize()) {
                    RouteInfoPanel(
                        routeInfo = info,
                        isMinimized = isRouteInfoMinimized,
                        onMinimizedChange = { routingViewModel.toggleRouteInfoMinimized() },
                        modifier = Modifier
                            .align(if (isRouteInfoMinimized) Alignment.BottomStart else Alignment.BottomCenter)
                            .padding(bottom = 16.dp, start = if (isRouteInfoMinimized) 16.dp else 0.dp)
                    )
                }
            }
        }
    }
}

