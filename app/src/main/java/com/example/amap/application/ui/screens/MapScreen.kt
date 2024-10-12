package com.example.amap.application.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.amap.api.maps.model.CameraPosition
import com.amap.api.maps.model.LatLng
import com.example.amap.application.viewmodels.MapViewModel
import com.example.amap.application.ui.components.map.AMapComposable
import com.example.amap.application.ui.components.map.rememberCameraPositionState

@Composable
fun MapScreen(viewModel: MapViewModel = hiltViewModel()) {
    var cameraPositionState by remember { mutableStateOf(rememberCameraPositionState()) }

    Box(modifier = Modifier.fillMaxSize()) {
        AMapComposable(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        )

        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            FloatingActionButton(
                onClick = { viewModel.startLocationTracking() },
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "定位"
                )
            }

            FloatingActionButton(
                onClick = { 
                    cameraPositionState = cameraPositionState.copy(
                        position = cameraPositionState.position.copy(
                            zoom = (cameraPositionState.position.zoom + 1).coerceAtMost(20f)
                        )
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
                    cameraPositionState = cameraPositionState.copy(
                        position = cameraPositionState.position.copy(
                            zoom = (cameraPositionState.position.zoom - 1).coerceAtLeast(3f)
                        )
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "缩小"
                )
            }
        }
    }
}
