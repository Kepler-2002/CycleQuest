package com.cyclequest.application.ui.component.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.model.LatLng
import com.amap.api.maps2d.model.PolygonOptions
import com.cyclequest.application.viewmodels.ProvinceState

import timber.log.Timber

@Composable
fun DiscoveryLayer(
    aMap: AMap,
    boundaryPoints: List<LatLng>,
    provinceState: ProvinceState,
    strokeWidth: Float = 5f
) {
    val strokeColor = when (provinceState) {
        ProvinceState.DEFAULT -> Color(0xFFB0B0B0) // 半透明灰色
        ProvinceState.EXPLORED -> Color(0xFF4CAF50) // 半透明绿色
    }
    val fillColor = when (provinceState) {
        ProvinceState.DEFAULT -> Color(0x26B0B0B0) // 15% 透明度的灰色
        ProvinceState.EXPLORED -> Color(0x4D4CAF50) // 30% 透明度的绿色
    }

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