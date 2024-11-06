package com.cyclequest.application.ui.component.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.runtime.DisposableEffect
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.model.LatLng
import com.amap.api.maps2d.model.PolylineOptions

@Composable
fun RoutingLayer(
    aMap: AMap,
    routePoints: List<LatLng> = emptyList(),
    strokeWidth: Float = 10f,
    strokeColor: Color = Color(0xFFFF9800),  // Material Green
) {
    if (routePoints.isNotEmpty()) {
        DisposableEffect(routePoints) {
            val polyline = PolylineOptions().apply {
                addAll(routePoints)
                width(strokeWidth)
                color(strokeColor.toArgb())
            }.let { aMap.addPolyline(it) }

            onDispose {
                polyline.remove()
            }
        }
    }
}

