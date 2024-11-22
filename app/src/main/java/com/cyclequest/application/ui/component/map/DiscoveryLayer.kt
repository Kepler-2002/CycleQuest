package com.cyclequest.application.ui.component.map

import androidx.compose.animation.animateColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.animation.core.*
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.model.LatLng
import com.amap.api.maps2d.model.PolygonOptions
import com.cyclequest.application.viewmodels.ProvinceState

@Composable
fun DiscoveryLayer(
    aMap: AMap,
    boundaryPoints: List<LatLng>,
    provinceState: ProvinceState,
    strokeWidth: Float = 5f
) {
    // 创建过渡动画
    val transition = updateTransition(
        targetState = provinceState,
        label = "colorTransition"
    )

    // 描边颜色动画
    val strokeColor by transition.animateColor(
        transitionSpec = { 
            tween(
                durationMillis = 500,
                easing = FastOutSlowInEasing
            )
        },
        label = "strokeColor"
    ) { state ->
        when (state) {
            ProvinceState.DEFAULT -> Color(0xFFB0B0B0)
            ProvinceState.EXPLORED -> Color(0xFF4CAF50)
        }
    }

    // 填充颜色动画
    val fillColor by transition.animateColor(
        transitionSpec = { 
            tween(
                durationMillis = 500,
                easing = FastOutSlowInEasing
            )
        },
        label = "fillColor"
    ) { state ->
        when (state) {
            ProvinceState.DEFAULT -> Color(0x26B0B0B0)
            ProvinceState.EXPLORED -> Color(0x4D4CAF50)
        }
    }

    // 绘制多边形
    if (boundaryPoints.isNotEmpty()) {
        DisposableEffect(boundaryPoints, strokeColor, fillColor) {
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