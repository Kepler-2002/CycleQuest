package com.cyclequest.application.ui.component.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.model.LatLng
import com.amap.api.maps2d.model.PolygonOptions
import timber.log.Timber

@Composable
fun DiscoveryLayer(
    aMap: AMap,
    boundaryPoints: List<LatLng>,
) {
    if (boundaryPoints.isNotEmpty()) {
        DisposableEffect(boundaryPoints) {
            val polygon = PolygonOptions().apply {
                addAll(boundaryPoints)
                strokeWidth(5f)
                strokeColor(android.graphics.Color.argb(255, 76, 175, 80))
                fillColor(android.graphics.Color.argb(40, 76, 175, 80))
            }.let { aMap.addPolygon(it) }

            onDispose {
                polygon.remove()
            }
        }
    }
}