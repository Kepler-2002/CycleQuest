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
import androidx.compose.ui.graphics.lerp
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
                durationMillis = 1500,
                easing = FastOutSlowInEasing
            )
        },
        label = "strokeColor"
    ) { state ->
        when (state) {
            ProvinceState.DEFAULT -> Color(0xFF1A237E)  // 深蓝色未探索
            ProvinceState.EXPLORED -> Color(0xFF64FFDA)  // 青绿色已探索
        }
    }

    // 发光效果动画
    val glowProgress by transition.animateFloat(
        transitionSpec = {
            if (targetState == ProvinceState.EXPLORED) {
                keyframes {
                    durationMillis = 1500
                    0f at 0
                    1f at 600    // 600ms达到最亮
                    0.7f at 900 // 900ms稍微暗一点
                    0.9f at 1200 // 1200ms再次发亮
                    0.6f at 1500 // 最终亮度
                }
            } else {
                tween(durationMillis = 1500)
            }
        },
        label = "glowProgress"
    ) { state ->
        when (state) {
            ProvinceState.DEFAULT -> 0f
            ProvinceState.EXPLORED -> 0f
        }
    }

    // 填充颜色动画
    val fillColor by transition.animateColor(
        transitionSpec = { 
            tween(
                durationMillis = 1500,
                easing = FastOutSlowInEasing
            )
        },
        label = "fillColor"
    ) { state ->
        when (state) {
            ProvinceState.DEFAULT -> Color(0x401A237E)  // 半透明深蓝色
            ProvinceState.EXPLORED -> {
                val baseColor = Color(0xFF00BFA5)  // 基础青绿色
                val glowColor = Color(0xFF64FFDA)  // 发光时的亮青色
                lerp(
                    baseColor,
                    glowColor,
                    FastOutSlowInEasing.transform(glowProgress)
                ).copy(alpha = 0.7f)  // 提高透明度以增加对比度
            }
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