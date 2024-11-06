package com.cyclequest.application.ui.component.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.runtime.DisposableEffect
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.model.LatLng
import com.amap.api.maps2d.model.PolylineOptions
import com.amap.api.maps2d.model.MarkerOptions
import com.amap.api.maps2d.model.BitmapDescriptorFactory
import com.amap.api.maps2d.model.Marker

@Composable
fun RoutingLayer(
    aMap: AMap,
    routePoints: List<LatLng> = emptyList(),
    strokeWidth: Float = 12f,
    strokeColor: Color = Color(0xFF2196F3),
) {
    if (routePoints.isNotEmpty()) {
        DisposableEffect(routePoints) {
            // 主路线
            val mainPolyline = PolylineOptions().apply {
                addAll(routePoints)
                width(strokeWidth)
                color(strokeColor.toArgb())
                zIndex(2f)
                geodesic(true)
            }.let { aMap.addPolyline(it) }

            // 起点标记
            val startMarker = MarkerOptions().apply {
                position(routePoints.first())
                icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                title("起点")
                zIndex(3f)
            }.let { aMap.addMarker(it) }

            // 终点标记
            val endMarker = MarkerOptions().apply {
                position(routePoints.last())
                icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                title("终点")
                zIndex(3f)
            }.let { aMap.addMarker(it) }

            onDispose {
                mainPolyline.remove()
                startMarker.remove()
                endMarker.remove()
            }
        }
    }
}

