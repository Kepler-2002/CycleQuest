package com.cyclequest.application.ui.component.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.amap.api.maps2d.model.LatLng
import com.amap.api.maps2d.model.PolygonOptions
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

@Composable
fun Polygon(
    points: List<LatLng>,
    strokeWidth: Float = 2f,
    strokeColor: Color = Color.Black,
    fillColor: Color = Color(0x33FF0000),
    modifier: Modifier = Modifier
) {
//    TODO: find where is LocalAMap and uncomment codes below
//    val map = LocalAMap.current
//
//    DisposableEffect(points) {
//        val polygon = PolygonOptions().apply {
//            addAll(points)
//            strokeWidth(strokeWidth)
//            strokeColor(strokeColor.toArgb())
//            fillColor(fillColor.toArgb())
//        }.let { map.addPolygon(it) }
//
//        onDispose {
//            polygon.remove()
//        }
//    }
} 