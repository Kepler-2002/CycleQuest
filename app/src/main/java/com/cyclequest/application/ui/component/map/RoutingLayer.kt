package com.cyclequest.application.ui.component.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun RoutingLayer() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x80000000)) // 半透明黑色背景
    ) {
        Text(
            text = "路线覆盖层",
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

