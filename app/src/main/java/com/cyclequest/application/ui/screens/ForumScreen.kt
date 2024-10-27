package com.cyclequest.application.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextOverflow  // Add this import


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForumScreen() {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("个人空间") },
                actions = {
                    IconButton(onClick = { /* TODO: Implement close action */ }) {
                        Icon(Icons.Default.Close, contentDescription = "关闭")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Status tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text("正在进行", color = Color.Green, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(16.dp))
                Text("即将上新", color = Color.Gray)
            }

            // My awards space
            Text(
                "我的奖牌",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleMedium
            )

            // Awards row
            LazyRow(
                modifier = Modifier.padding(
                    horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(5) { index ->
                    AwardItem(index)
                }
            }

            // My post space
            Text(
                "我的帖子",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleMedium
            )

            // Posts row
            LazyRow(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(3) { index ->
                    PostItem(index)
                }
            }

            // Category tabs
            TabRow(selectedTabIndex = 0) {
                listOf("推荐", "发帖", "邀请有礼", "晒奖牌").forEachIndexed { index, title ->
                    Tab(
                        selected = index == 0,
                        onClick = { /* TODO: Implement tab selection */ },
                        text = { Text(title) }
                    )
                }
            }

            // Award events
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(2) { index ->
                    AwardEventCard(index)
                }
            }
        }
    }
}

@Composable
fun AwardItem(index: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = android.R.drawable.ic_menu_gallery), // Replace with actual image resource
            contentDescription = null,
            modifier = Modifier.size(60.dp),
            contentScale = ContentScale.Fit
        )
        Text(
            when (index) {
                0 -> "活动1"
                1 -> "活动2"
                2 -> "活动3"
                else -> "活动${index + 1}"
            },
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun AwardEventCard(index: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = android.R.drawable.ic_menu_gallery), // Replace with actual image resource
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    if (index == 0) "龙盘祥瑞 | 2024 线上北京马拉松" else "梦见云端 | 哆啦A梦神奇道具线上跑",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "报名时间: 10.12 - 12.11",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    if (index == 0) "2203 人已参与" else "12657 人已参与",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun PostItem(index: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(120.dp)
    ) {
        Image(
            painter = painterResource(id = android.R.drawable.ic_menu_gallery), // Replace with actual image resource
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .aspectRatio(1f),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            "帖子 ${index + 1}",
            style = MaterialTheme.typography.bodySmall,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}
