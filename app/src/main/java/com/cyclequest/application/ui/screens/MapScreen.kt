package com.cyclequest.application.ui.screens

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import com.cyclequest.application.ui.components.map.AMapComposable
import com.cyclequest.application.ui.components.map.rememberCameraPositionState
import com.cyclequest.application.viewmodels.MapViewModel
import com.amap.api.maps2d.model.CameraPosition
import com.amap.api.maps2d.model.LatLng
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.isGranted
import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import com.cyclequest.application.ui.component.map.PillButton
import com.cyclequest.application.ui.component.map.Polygon



@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(viewModel: MapViewModel = hiltViewModel()) {
    val cameraPositionState = rememberCameraPositionState()
    val currentLocation by viewModel.currentLocation.collectAsState()
    val permissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val boundaryPoints by viewModel.boundaryPoints.collectAsState()

    var selectedOption by remember { mutableStateOf("地图") }
    val options = listOf("地图", "路线", "探索")

    LaunchedEffect(Unit) {
        if (!permissionState.status.isGranted) {
            permissionState.launchPermissionRequest()
        }
    }

    LaunchedEffect(currentLocation) {
        currentLocation?.let { location ->
            cameraPositionState.position = CameraPosition(
                LatLng(location.latitude, location.longitude),
                cameraPositionState.position.zoom,
                cameraPositionState.position.tilt,
                cameraPositionState.position.bearing
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadAdministrativeBoundary("810016") // 这里使用沙田区的编码
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AMapComposable(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        )
//      TODO: The commented codes caused error, fix them
//        {
//            // 添加多边形覆盖物
//            if (boundaryPoints.isNotEmpty()) {
//                Polygon(
//                    points = boundaryPoints,
//                    strokeWidth = 2f,
//                    strokeColor = Color.Black,
//                    fillColor = Color(0x33FF0000) // 半透明红色
//                )
//            }
//        }

        // 添加 PillButton 到顶部并居中对齐
        PillButton(
            options = options,
            selectedOption = selectedOption,
            onOptionSelected = { selectedOption = it },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
        )

        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            FloatingActionButton(
                onClick = { viewModel.getCurrentLocation() },
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "定位"
                )
            }

            FloatingActionButton(
                onClick = { 
                    val newZoom = (cameraPositionState.position.zoom + 1).coerceAtMost(20f)
                    cameraPositionState.position = CameraPosition(
                        cameraPositionState.position.target,
                        newZoom,
                        cameraPositionState.position.tilt,
                        cameraPositionState.position.bearing
                    )
                },
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "放大"
                )
            }

            FloatingActionButton(
                onClick = { 
                    val newZoom = (cameraPositionState.position.zoom - 1).coerceAtLeast(3f)
                    cameraPositionState.position = CameraPosition(
                        cameraPositionState.position.target,
                        newZoom,
                        cameraPositionState.position.tilt,
                        cameraPositionState.position.bearing
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "缩小"
                )
            }
        }
    }
}
