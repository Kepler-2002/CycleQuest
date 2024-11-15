package com.cyclequest.application.ui.component.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.runtime.DisposableEffect
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.model.*
import java.lang.Math

@Composable
fun RoutingLayer(
    aMap: AMap,
    routePoints: List<LatLng> = emptyList(),
    strokeWidth: Float = 12f,
    strokeColor: Color = Color(0xFF2196F3),
) {
    if (routePoints.isNotEmpty()) {
        DisposableEffect(routePoints) {
            // 阴影效果
            val shadowPolyline = aMap.addPolyline(PolylineOptions().apply {
                addAll(routePoints)
                width(strokeWidth + 4f)
                color(0x29000000)  // 半透明黑色阴影
                zIndex(1f)
            })

            // 主路线
            val mainPolyline = aMap.addPolyline(PolylineOptions().apply {
                addAll(routePoints)
                width(strokeWidth)
                color(strokeColor.toArgb())
                zIndex(2f)
                geodesic(true)
            })

            // 转弯点标记
            val turnPoints = mutableListOf<Circle>()
            routePoints.forEachIndexed { index, point ->
                if (index > 0 && index < routePoints.size - 1) {
                    val prevPoint = routePoints[index - 1]
                    val nextPoint = routePoints[index + 1]
                    if (isDirectionChanged(prevPoint, point, nextPoint)) {
                        val circle = aMap.addCircle(CircleOptions().apply {
                            center(point)
                            radius(4.0)
                            fillColor(Color.White.toArgb())
                            strokeColor(strokeColor.toArgb())
                            strokeWidth(2f)
                            zIndex(3f)
                        })
                        turnPoints.add(circle)
                    }
                }
            }

            // 起点标记
            val startMarker = aMap.addMarker(MarkerOptions().apply {
                position(routePoints.first())
                icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                title("起点")
                zIndex(4f)
            })

            // 终点标记
            val endMarker = aMap.addMarker(MarkerOptions().apply {
                position(routePoints.last())
                icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                title("终点")
                zIndex(4f)
            })

            onDispose {
                shadowPolyline.remove()
                mainPolyline.remove()
                turnPoints.forEach { it.remove() }
                startMarker.remove()
                endMarker.remove()
            }
        }
    }
}

private fun isDirectionChanged(prev: LatLng, current: LatLng, next: LatLng): Boolean {
    val angle1 = Math.atan2(
        current.latitude - prev.latitude,
        current.longitude - prev.longitude
    )
    val angle2 = Math.atan2(
        next.latitude - current.latitude,
        next.longitude - current.longitude
    )
    val angleDiff = Math.abs(angle1 - angle2)
    return angleDiff > Math.PI / 6  // 转弯角度大于30度时显示转弯点
}

