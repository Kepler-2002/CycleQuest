package com.cyclequest.application.ui.component.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.model.LatLng
import com.amap.api.maps2d.model.PolygonOptions
import timber.log.Timber

@Composable
fun DiscoveryLayer(aMap: AMap, boundaryPoints: List<LatLng>) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x80000000)) // 半透明黑色背景
    ) {
        if (boundaryPoints.isNotEmpty()) {
            Timber.d("Boundary Points: $boundaryPoints") // 打印边界点日志

            val polygonOptions = PolygonOptions()
                .addAll(boundaryPoints)
                .strokeWidth(15f)
                .strokeColor(android.graphics.Color.argb(50, 0, 0, 255)) // 半透明蓝色边框
                .fillColor(android.graphics.Color.argb(128, 0, 0, 255)) // 半透明蓝色填充

            aMap.addPolygon(polygonOptions)
        }
    }
}