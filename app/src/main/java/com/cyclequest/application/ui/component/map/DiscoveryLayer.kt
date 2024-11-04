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

@Composable
fun DiscoveryLayer(aMap: AMap) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x80000000)) // 半透明黑色背景
    ) {
        val points = listOf(
            LatLng(42.742467, 79.842785),
            LatLng(43.893433, 98.124035),
            LatLng(33.058738, 101.463879),
            LatLng(25.873426, 95.838879),
            LatLng(30.8214661, 78.788097)
        )

        val polygonOptions = PolygonOptions()
            .addAll(points)
            .strokeWidth(15f)
            .strokeColor(android.graphics.Color.argb(50, 0, 0, 255)) // 半透明蓝色边框
            .fillColor(android.graphics.Color.argb(128, 0, 0, 255)) // 半透明蓝色填充

        aMap.addPolygon(polygonOptions)
    }
}