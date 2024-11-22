package com.cyclequest.application.ui.screens

import android.util.Log
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
import com.cyclequest.application.ui.component.forum.AwardEventCard
import com.cyclequest.application.ui.component.forum.PostItem
import com.cyclequest.application.viewmodels.CreatePostViewModel
import com.cyclequest.application.viewmodels.ForumViewModel

import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.sp

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.MaterialTheme


import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Public
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.cyclequest.application.viewmodels.RegistrationViewModel
import com.cyclequest.data.local.entity.PostStatus
import com.cyclequest.data.local.preferences.UserPreferences
import com.cyclequest.domain.model.Post
import com.cyclequest.domain.repository.PostRepository

import androidx.hilt.navigation.compose.hiltViewModel
import com.cyclequest.data.local.entity.PostEntity
import com.cyclequest.domain.model.Achievement
import com.cyclequest.domain.model.PostImages
import kotlinx.coroutines.launch


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
    var selectedImageResource by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(achievementId) {
        achievementId?.let {
            val achievement = viewModel.getAchievementById(it)
            selectedImageResource = achievement?.resourceId
        }
    }

    // 其他代码...

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
        color = Color.Black,
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
                        tint = Color.White
                    )
                }

                TextButton(
                    onClick = { handleSubmit() },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.White.copy(alpha = 0.6f)
                    )
                ) {
                    Text("发表", fontSize = 16.sp)
                }
            }

            TextField(
                value = postText,
                onValueChange = { newText -> 
                    postText = newText // 更新本地状态
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                label = { Text("这一刻的想法...") },
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.White,
                    focusedLabelColor = Color.Gray,
                    unfocusedLabelColor = Color.Gray
                ),
                singleLine = false,
                shape = MaterialTheme.shapes.small
            )

            // 新增的标题输入框
            TextField(
                value = titleText,
                onValueChange = { newText -> 
                    titleText = newText // 更新本地状态
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp), // 添加顶部间距
                label = { Text("标题") },
                placeholder = { Text("请输入标题") }, // 添加placeholder
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.White,
                    focusedLabelColor = Color.Gray,
                    unfocusedLabelColor = Color.Gray
                ),
                isError = titleText.isEmpty() // 如果标题为空，显示错误状态
            )

            // 奖牌选择框
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.DarkGray.copy(alpha = 0.3f))
                    .clickable {
                        showDialog = true // 显示奖牌选择对话框
                    },
                contentAlignment = Alignment.Center
            ) {
                selectedImageResource?.let {
                    Image(
                        painter = painterResource(id = it),
                        contentDescription = "Selected Achievement",
                        modifier = Modifier.size(60.dp)
                    )
                } ?: Text("选择奖牌")
            }

            // Bottom Options
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                ListItem(
                    leadingContent = {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = Color.White
                        )
                    },
                    headlineContent = { Text("所在位置", color = Color.White) },
                    trailingContent = {
                        Icon(
                            Icons.Default.ChevronRight,
                            contentDescription = "Select",
                            tint = Color.Gray
                        )
                    },
                    modifier = Modifier.clickable { /* TODO: Implement location selection */ }
                )

                ListItem(
                    leadingContent = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Mention",
                            tint = Color.White
                        )
                    },
                    headlineContent = { Text("提醒谁看", color = Color.White) },
                    trailingContent = {
                        Icon(
                            Icons.Default.ChevronRight,
                            contentDescription = "Select",
                            tint = Color.Gray
                        )
                    },
                    modifier = Modifier.clickable { /* TODO: Implement mention */ }
                )

                ListItem(
                    leadingContent = {
                        Icon(
                            Icons.Default.Public,
                            contentDescription = "Visibility",
                            tint = Color.White
                        )
                    },
                    headlineContent = { Text("谁可以看", color = Color.White) },
                    trailingContent = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("公开", color = Color.Gray)
                            Icon(
                                Icons.Default.ChevronRight,
                                contentDescription = "Select",
                                tint = Color.Gray
                            )
                        }
                    },
                    modifier = Modifier.clickable { /* TODO: Implement visibility settings */ }
                )
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
}



@Preview(showBackground = true)
@Composable
fun CreatePostScreenPreview() {
    CreatePostScreen(onNavigateBack = {}, achievementId = "")
}