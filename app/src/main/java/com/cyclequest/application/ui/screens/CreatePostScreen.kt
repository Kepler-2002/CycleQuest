package com.cyclequest.application.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cyclequest.application.viewmodels.CreatePostViewModel


import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.sp

import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.MaterialTheme

import com.cyclequest.data.local.entity.PostStatus
import com.cyclequest.domain.model.Post

import androidx.hilt.navigation.compose.hiltViewModel



@Composable
fun AwardItem(viewModel: CreatePostViewModel = hiltViewModel()) {
    val achievements by viewModel.getAchievements().collectAsState(initial = emptyList())

    LazyRow {
        items(achievements) { achievement ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = achievement.resourceId),
                    contentDescription = null,
                    modifier = Modifier.size(60.dp),
                    contentScale = ContentScale.Fit
                )
                Text(
                    achievement.name,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    onNavigateBack: () -> Unit,
    achievementId: String?, // 接收Achievement ID
    viewModel: CreatePostViewModel = hiltViewModel()
) {
    // 添加位置状态
    var showLocationDialog by remember { mutableStateOf(false) }
    var locationText by remember { mutableStateOf("") }
    var currentLocation by remember { mutableStateOf("所在位置") }
    var selectedImageResource by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(achievementId) {
        achievementId?.let {
            val achievement = viewModel.getAchievementById(it)
            selectedImageResource = achievement?.resourceId
        }
    }

    val darkColorScheme = darkColorScheme()

    var selectedImagePath by remember { mutableStateOf<String?>(null) }

    var showDialog by remember { mutableStateOf(false) }

    var postText by remember { mutableStateOf("") }



    // 奖牌选择框
    var selectedAwardIndex by remember { mutableStateOf(2) } // 用于存储选择的奖牌索引

    // 新增的标题输入框
    var titleText by remember { mutableStateOf("") }

    // 获取成就列表
    val achievements by viewModel.getAchievements().collectAsState(initial = emptyList())

    // 奖牌选择框点击事件
    Box(modifier = Modifier.clickable { showDialog = true }) {
        selectedImageResource?.let {
            Image(
                painter = painterResource(id = it),
                contentDescription = "Selected Achievement",
                modifier = Modifier.size(60.dp)
            )
        } ?: Text("选择奖牌")
    }

    fun handleSubmit() {
        
        // 创建一个 Post 对象
        val post = Post(
            postId = "",
            userId = viewModel.userPreferences.getUser()?.id ?: "",
            title = titleText, // 使用标题文本
            content = postText,
            achievementId = "",
            viewCount = 0,
            likeCount = 0,
            commentCount = 0,
            isTop = false,
            status = PostStatus.NORMAL,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        
        // 日志输出
        Log.d("CreatePostScreen", "Attempting to save post: $post")
        
        // 调用 ViewModel 的 savePost 方法
        viewModel.savePost(post)
        
        // 提交后清空输入框并返回
        postText = ""
        titleText = "" // 清空标题
        onNavigateBack() // 返回到 ForumScreen
    }

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                Button(
                    onClick = { handleSubmit() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("发表", fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 标题输入框
            TextField(
                value = titleText,
                onValueChange = { titleText = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("标题") },
                placeholder = { Text("请输入标题") },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.outline
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 奖牌选择区域 - 独占一行
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        shape = MaterialTheme.shapes.medium
                    )
                    .clickable { showDialog = true },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    selectedImageResource?.let {
                        Image(
                            painter = painterResource(id = it),
                            contentDescription = "Selected Achievement",
                            modifier = Modifier.size(60.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "点击更换奖牌",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } ?: Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Add Medal",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "选择奖牌",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 主要内容输入框
            TextField(
                value = postText,
                onValueChange = { postText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                label = { Text("这一刻的想法...") },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.outline
                ),
                singleLine = false
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 底部功能区
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 所在位置
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showLocationDialog = true },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = currentLocation,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("选择奖牌") },
            text = {
                LazyColumn {
                    items(achievements) { achievement ->
                        ListItem(
                            modifier = Modifier.clickable {
                                selectedImageResource = achievement.resourceId
                                showDialog = false
                            },
                            headlineContent = { Text(achievement.name) }
                        )
                    }
                }
            },
            confirmButton = {
                Button(onClick = { showDialog = false }) {
                    Text("确认")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("取消")
                }
            }
        )
    }
    // 位置输入弹窗
    if (showLocationDialog) {
        AlertDialog(
            onDismissRequest = { showLocationDialog = false },
            title = { Text("输入位置") },
            text = {
                TextField(
                    value = locationText,
                    onValueChange = { locationText = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("请输入位置") },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.outline
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (locationText.isNotBlank()) {
                            currentLocation = locationText
                        }
                        showLocationDialog = false
                        locationText = "" // 清空输入框
                    }
                ) {
                    Text("完成")
                }
            },
            dismissButton = {
                Button(onClick = { 
                    showLocationDialog = false
                    locationText = "" // 清空输入框
                }) {
                    Text("取消")
                }
            }
        )
    }
}



@Preview(showBackground = true)
@Composable
fun CreatePostScreenPreview() {
    CreatePostScreen(onNavigateBack = {}, achievementId = "")
}