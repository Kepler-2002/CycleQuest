package com.cyclequest.application.ui.component.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.model.LatLng
import com.amap.api.maps2d.model.PolygonOptions
import timber.log.Timber

@Composable
fun DiscoveryLayer(
    aMap: AMap,
    boundaryPoints: List<LatLng>,
    strokeWidth: Float = 5f,
    strokeColor: Color = Color(0xFF4CAF50),  // Material Green
    fillColor: Color = Color(0x284CAF50),    // 透明度约15%的绿色
    isLight : Boolean = false // 默认没有点亮
) {
    if (boundaryPoints.isNotEmpty()) {
        DisposableEffect(boundaryPoints) {
            val polygon = PolygonOptions().apply {
                addAll(boundaryPoints)
                strokeWidth(strokeWidth)
                strokeColor(strokeColor.toArgb())
                fillColor(fillColor.toArgb())
            }.let { aMap.addPolygon(it) }

            onDispose {
                polygon.remove()
            }
        }
    }
}