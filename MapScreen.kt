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

@Composable
fun PillButton(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val buttonWidth = screenWidth / 2

    Row(
        modifier = modifier
            .width(buttonWidth)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(24.dp)
            )
            .padding(4.dp)
    ) {
        options.forEach { option ->
            Button(
                onClick = { onOptionSelected(option) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (option == selectedOption)
                        MaterialTheme.colorScheme.primary
                    else
                        Color.Transparent,
                    contentColor = if (option == selectedOption)
                        MaterialTheme.colorScheme.onPrimary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(36.dp)
            ) {
                Text(text = option)
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(viewModel: MapViewModel = hiltViewModel()) {
    var selectedOption by remember { mutableStateOf("地图") }
    val options = listOf("地图", "路线", "探索")

    Box(modifier = Modifier.fillMaxSize()) {
        // 基础地图界面始终显示
        MapContent(viewModel)

        // 覆盖层界面
        when (selectedOption) {
            "路线" -> RouteOverlay()
            "探索" -> ExploreOverlay()
        }

        // 顶部选项按钮
        PillButton(
            options = options,
            selectedOption = selectedOption,
            onOptionSelected = { selectedOption = it },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
        )
    }
}

// 基础地图界面
@Composable
private fun MapContent(viewModel: MapViewModel) {
    Box(modifier = Modifier.fillMaxSize()) {
        // 地图组件
        AMapComposable(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = rememberCameraPositionState()
        )

        // 右侧控制按钮
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            FloatingActionButton(
                onClick = { viewModel.getCurrentLocation() },
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(Icons.Default.LocationOn, "定位")
            }

            FloatingActionButton(
                onClick = { /* 放大逻辑 */ },
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(Icons.Default.Add, "放大")
            }

            FloatingActionButton(
                onClick = { /* 缩小逻辑 */ }
            ) {
                Text("-", style = MaterialTheme.typography.headlineMedium)
            }
        }
    }
}

// 路线覆盖层 - 绿色半透明背景
@Composable
private fun RouteOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Green.copy(alpha = 0.9f)),  // 90%不透明度的绿色
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "路线规划",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White
        )
    }
}

// 探索覆盖层 - 黑色半透明背景
@Composable
private fun ExploreOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.9f)),  // 90%不透明度的黑色
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "探索发现",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White
        )
    }
}
