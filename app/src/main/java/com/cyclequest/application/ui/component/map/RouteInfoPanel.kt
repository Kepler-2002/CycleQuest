package com.cyclequest.application.ui.component.map

import kotlinx.coroutines.launch
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import com.cyclequest.service.route.RouteService
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteInfoPanel(
    routeInfo: RouteService.RouteInfo,
    isMinimized: Boolean,
    onMinimizedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var isDetailsExpanded by remember { mutableStateOf(false) }

    if (isMinimized) {
        // 最小化状态 - 显示浮动按钮
        FloatingActionButton(
            onClick = { onMinimizedChange(false) },
            modifier = Modifier
                .padding(16.dp)
                .size(56.dp),
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "展开路线信息"
            )
        }
    } else {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 72.dp),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { onMinimizedChange(true) },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "最小化"
                        )
                    }
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "总距离：${routeInfo.totalDistance / 1000.0}公里",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "预计时间：${routeInfo.totalDuration / 60}分钟",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    
                    IconButton(onClick = { isDetailsExpanded = !isDetailsExpanded }) {
                        Icon(
                            imageVector = if (isDetailsExpanded) 
                                Icons.Default.KeyboardArrowDown 
                            else 
                                Icons.Default.KeyboardArrowUp,
                            contentDescription = if (isDetailsExpanded) "收起详情" else "展开详情"
                        )
                    }
                }

                AnimatedVisibility(
                    visible = isDetailsExpanded,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 300.dp)
                    ) {
                        items(routeInfo.steps) { step ->
                            RouteStepItem(step)
                            Divider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RouteStepItem(
    step: RouteService.RouteStep
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = step.instruction,
                onTextLayout = {}
            )
            Text(
                text = step.road,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                onTextLayout = {}
            )
        }
        Text(
            text = "${step.distance}米",
            style = MaterialTheme.typography.bodyMedium,
            onTextLayout = {}
        )
    }
}