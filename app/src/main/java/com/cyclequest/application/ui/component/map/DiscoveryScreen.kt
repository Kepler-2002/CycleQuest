package com.cyclequest.application.ui.component.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.amap.api.maps2d.model.LatLng
import com.cyclequest.application.ui.component.map.Polygon

@Composable
fun ExploreOverlay(boundaryPoints: List<LatLng>) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x80000000)) // 半透明黑色背景
    ) {
        if (boundaryPoints.isNotEmpty()) {
            // 使用 Polygon 绘制边界
            Polygon(
                points = boundaryPoints,
                fillColor = Color(0x8000FFFF), // 半透明蓝色
                strokeColor = Color.Blue,
                strokeWidth = 2f
            )
        }
    }
}

