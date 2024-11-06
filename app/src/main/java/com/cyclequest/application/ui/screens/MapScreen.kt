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
import androidx.compose.foundation.lazy.LazyColumn
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.model.LatLng
import com.cyclequest.application.ui.component.map.PillButton
import com.cyclequest.application.ui.component.map.RoutingLayer
import com.cyclequest.application.ui.component.map.DiscoveryLayer
import androidx.compose.foundation.lazy.items
import com.cyclequest.service.route.RouteService
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Divider

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(viewModel: MapViewModel = hiltViewModel()) {
    val mapMode by viewModel.mapMode.collectAsState()
    val currentLocation by viewModel.currentLocation.collectAsState()
    val routePoints by viewModel.routePoints.collectAsState()
    val routeInfo by viewModel.routeInfo.collectAsState()
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
        aMapInstance?.let { aMap ->
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

        // 添加路线信息面板
        routeInfo?.let { info ->
            RouteInfoPanel(
                routeInfo = info,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RouteInfoPanel(
    routeInfo: RouteService.RouteInfo,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )
    val scope = rememberCoroutineScope()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { 
                        scope.launch {
                            if (bottomSheetState.isVisible) {
                                bottomSheetState.hide()
                            } else {
                                bottomSheetState.show()
                            }
                        }
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "总距离：${routeInfo.totalDistance / 1000.0}公里",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier,
                        onTextLayout = {}
                    )
                    Text(
                        text = "预计时间：${routeInfo.totalDuration / 60}分钟",
                        style = MaterialTheme.typography.titleMedium,
                        onTextLayout = {}
                    )
                }
                Icon(
                    imageVector = if (bottomSheetState.isVisible) 
                        Icons.Default.KeyboardArrowDown 
                    else 
                        Icons.Default.KeyboardArrowUp,
                    contentDescription = if (bottomSheetState.isVisible) "收起" else "展开"
                )
            }

            if (bottomSheetState.isVisible) {
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(
                    modifier = Modifier.heightIn(max = 300.dp)
                ) {
                    items(routeInfo.steps) { step ->
                        RouteStepItem(step)
                        Divider()
                    }
                }
            }
        }
    }
}

@Composable
private fun RouteStepItem(
    step: RouteService.RouteStep
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = step.instruction,
                onTextLayout = {}
            )
            Text(
                text = step.road,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                onTextLayout = {}
            )
        }
        Text(
            text = "${step.distance}米",
            style = MaterialTheme.typography.bodyMedium,
            onTextLayout = {}
        )
    }
}
