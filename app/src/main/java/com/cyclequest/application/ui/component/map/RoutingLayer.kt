package com.cyclequest.application.ui.component.map

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.CameraUpdateFactory
import com.amap.api.maps2d.model.BitmapDescriptorFactory
import com.amap.api.maps2d.model.CameraPosition
import com.amap.api.maps2d.model.Circle
import com.amap.api.maps2d.model.CircleOptions
import com.amap.api.maps2d.model.LatLng
import com.amap.api.maps2d.model.MarkerOptions
import com.amap.api.maps2d.model.PolylineOptions
import com.cyclequest.application.viewmodels.MapViewModel
import com.cyclequest.application.viewmodels.RoutingViewModel


@Composable
fun RoutingLayer(
    aMap: AMap,
//    routePoints: List<LatLng> = emptyList(),
    strokeWidth: Float = 12f,
    strokeColor: Color = Color(0xFF2196F3),
    mapViewModel: MapViewModel,
    routingViewModel: RoutingViewModel = hiltViewModel(),
) {
    val routePoints by routingViewModel.routePoints.collectAsState()
    val routeInfo by routingViewModel.routeInfo.collectAsState()
    val isRouteInfoMinimized by routingViewModel.isRouteInfoMinimized.collectAsState()
    val isNavigationStarted by routingViewModel.isNavigationStarted.collectAsState()
    val isSimulateNaviOn by routingViewModel.isSimulateNaviOn.collectAsState()

    // 显示的搜索框
    Box(modifier = Modifier.fillMaxSize()) {
        SearchPanel(
            modifier = Modifier,
            routeInfo = routeInfo
        )
    }

    // 开始导航时
    if (isNavigationStarted) {
        // 显示面板
        routeInfo?.let { info ->
            Box(modifier = Modifier.fillMaxSize()) {
                RouteInfoPanel(
                    routeInfo = info,
                    isMinimized = isRouteInfoMinimized,
                    onMinimizedChange = { routingViewModel.toggleRouteInfoMinimized() },
                    modifier = Modifier
                        .align(if (isRouteInfoMinimized) Alignment.BottomStart else Alignment.BottomCenter)
                        .padding(bottom = 16.dp, start = if (isRouteInfoMinimized) 16.dp else 0.dp)
                )
            }
        }
    }

    // 显示路线(有routePoints时)
    if (routePoints.isNotEmpty()) {
        val latitudes = routePoints.map { it.latitude }
        val longitudes = routePoints.map { it.longitude }

        val minLat = latitudes.minOrNull() ?: 0.0
        val maxLat = latitudes.maxOrNull() ?: 0.0
        val minLng = longitudes.minOrNull() ?: 0.0
        val maxLng = longitudes.maxOrNull() ?: 0.0

        val centerPoint = LatLng((minLat + maxLat) / 2, (minLng + maxLng) / 2)

        // Calculate maximum span in latitude and longitude
        val latSpan = maxLat - minLat
        val lngSpan = maxLng - minLng

        // Calculate zoom level based on span (example calculation)
        val zoomLevel = calculateZoomLevel(latSpan, lngSpan)

        // Log the bounding rectangle, center point, and zoom level
        Log.i("RoutingLayer", "Bounding Rectangle: [($minLat, $minLng), ($maxLat, $maxLng)]")
        Log.i("RoutingLayer", "Center Point: $centerPoint")
        Log.i("RoutingLayer", "Calculated Zoom Level: $zoomLevel")

        DisposableEffect(routePoints) {
            // Update camera position with the new zoom level
            val mCameraUpdate = CameraUpdateFactory.newCameraPosition(
                CameraPosition(
                    centerPoint,
                    zoomLevel,
                    0f,
                    0f
                )
            )
            // 路线居中
            aMap.animateCamera(mCameraUpdate)


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

// Function to calculate zoom level based on latitude and longitude span
private fun calculateZoomLevel(latSpan: Double, lngSpan: Double): Float {
    // Example logic to determine zoom level based on span
    val maxSpan = maxOf(latSpan, lngSpan)
    return when {
        maxSpan < 0.01 -> 16.5f // Close zoom
        maxSpan < 0.1 -> 14.5f
        maxSpan < 1.0 -> 12.5f
        maxSpan < 10.0 -> 10.5f
        else -> 10f // Far zoom
    }
}

