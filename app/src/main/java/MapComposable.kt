//package com.cyclequest.application.ui.component.map
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Add
//import androidx.compose.material.icons.filled.LocationOn
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
//import com.cyclequest.application.ui.components.map.AMapComposable
//import com.cyclequest.application.ui.components.map.rememberCameraPositionState
////import com.cyclequest.application.viewmodels.MapViewModel
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.Alignment
//import androidx.compose.foundation.background
//import com.amap.api.maps2d.model.LatLng
//import com.cyclequest.application.viewmodels.MapViewModel
//
//
//@Composable
//fun MapScreen(viewModel: MapViewModel = hiltViewModel()) {
//    var selectedOption by remember { mutableStateOf("地图") }
//    val options = listOf("地图", "路线", "探索")
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        MapContent(viewModel)
//
//        when (selectedOption) {
//            "路线" -> com.cyclequest.application.ui.screens.RouteOverlay()
//            "探索" -> com.cyclequest.application.ui.screens.ExploreOverlay(boundaryPoints)
//        }
//
//        PillButton(
//            options = options,
//            selectedOption = selectedOption,
//            onOptionSelected = { selectedOption = it },
//            modifier = Modifier
//                .align(Alignment.TopCenter)
//                .padding(top = 16.dp)
//        )
//    }
//}
//
//@Composable
//private fun MapContent(viewModel: MapViewModel) {
//    Box(modifier = Modifier.fillMaxSize()) {
//        AMapComposable(
//            modifier = Modifier.fillMaxSize(),
//            cameraPositionState = rememberCameraPositionState()
//        )
//
//        Column(
//            modifier = Modifier
//                .align(Alignment.TopEnd)
//                .padding(16.dp)
//        ) {
//            FloatingActionButton(
//                onClick = { viewModel.getCurrentLocation() },
//                modifier = Modifier.padding(bottom = 16.dp)
//            ) {
//                Icon(Icons.Default.LocationOn, "定位")
//            }
//
//            FloatingActionButton(
//                onClick = { /* 放大逻辑 */ },
//                modifier = Modifier.padding(bottom = 8.dp)
//            ) {
//                Icon(Icons.Default.Add, "放大")
//            }
//
//            FloatingActionButton(
//                onClick = { /* 缩小逻辑 */ }
//            ) {
//                Text("-", style = MaterialTheme.typography.headlineMedium)
//            }
//        }
//    }
//}
//
//@Composable
//private fun RouteOverlay() {
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.Yellow.copy(alpha = 0.5f)),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = "路线规划",
//            style = MaterialTheme.typography.headlineMedium,
//            color = Color.Black
//        )
//    }
//}
//
//@Composable
//fun ExploreOverlay(boundaryPoints: List<LatLng>) {
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0x80000000)) // 半透明黑色背景
//    ) {
//        // 使用 Polygon 绘制边界
//        Polygon(
//            points = boundaryPoints,
//            fillColor = Color(0x8000FFFF), // 半透明蓝色
//            strokeColor = Color.Blue,
//            strokeWidth = 2f
//        )
//    }
//}
//
