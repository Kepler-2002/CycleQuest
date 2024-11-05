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
import com.amap.api.maps2d.model.LatLng
import com.cyclequest.application.ui.component.map.PillButton
import com.cyclequest.application.ui.component.map.RoutingLayer
import com.cyclequest.application.ui.component.map.DiscoveryLayer

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(viewModel: MapViewModel = hiltViewModel()) {
    val mapMode by viewModel.mapMode.collectAsState()
    val currentLocation by viewModel.currentLocation.collectAsState()
    var aMapInstance by remember { mutableStateOf<AMap?>(null) }
    val cameraPositionState = rememberCameraPositionState()

    // 权限处理
    val permissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    LaunchedEffect(Unit) {
        if (!permissionState.status.isGranted) {
            permissionState.launchPermissionRequest()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // 基础地图
        MapPage(
            modifier = Modifier.fillMaxSize(),
            onMapReady = { aMap -> aMapInstance = aMap }
        )

        // 根据模式显示不同图层
        aMapInstance?.let { aMap ->
            when (val mode = mapMode) {
                is MapViewModel.MapMode.Discovery -> {
                    DiscoveryLayer(
                        aMap = aMap,
                        boundaryPoints = mode.boundaryPoints
                    )
                }
                is MapViewModel.MapMode.Routing -> {
                    RoutingLayer(
                        aMap = aMap,
                        routePoints = listOf(  // 临时测试数据，后续应该从 ViewModel 获取
                            LatLng(39.999391, 116.135972),
                            LatLng(39.898323, 116.057694),
                            LatLng(39.900430, 116.265061),
                            LatLng(39.955192, 116.140092)
                        )
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
            selectedOption = when(mapMode) {
                is MapViewModel.MapMode.Default -> "地图"
                is MapViewModel.MapMode.Routing -> "路线"
                is MapViewModel.MapMode.Discovery -> "探索"
            },
            onOptionSelected = viewModel::setMapMode,
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
                onClick = { viewModel.updateCurrentLocation() },
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "定位"
                )
            }
        }
    }
}
