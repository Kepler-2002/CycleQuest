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
import com.cyclequest.application.ui.component.forum.AwardItem
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
import com.cyclequest.domain.model.Achievement

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    onNavigateBack: () -> Unit,
    viewModel: CreatePostViewModel = hiltViewModel()
) {
    val darkColorScheme = darkColorScheme()

    var selectedImagePath by remember { mutableStateOf<String?>(null) }

    var showDialog by remember { mutableStateOf(false) }

    var postText by remember { mutableStateOf("") }

    var selectedImageResource by remember { mutableStateOf<Int?>(null) }

    // 奖牌选择框
    var selectedAwardIndex by remember { mutableStateOf(2) } // 用于存储选择的奖牌索引

    // 新增的标题输入框
    var titleText by remember { mutableStateOf("") }

    fun handleSubmit() {
        
        // 创建一个 Post 对象
        val post = Post(
            postId = "",
            userId = viewModel.userPreferences.getUser()?.id ?: "",
            title = titleText, // 使用标题文本
            content = postText,
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

            TextField(
                value = postText,
                onValueChange = { newText -> 
                    postText = newText
                },
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
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                singleLine = false
            )

            // 新增的标题输入框
            TextField(
                value = titleText,
                onValueChange = { newText -> 
                    titleText = newText
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                label = { Text("标题") },
                placeholder = { Text("请输入标题") },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                isError = titleText.isEmpty()
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
                AwardItem(selectedAwardIndex) // 显示当前选择的奖牌
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
                    items(2) { index -> // 假设有3个奖牌
                        ListItem(
                            modifier = Modifier.clickable {
                                selectedAwardIndex = index // 更新选择的奖牌索引
                                showDialog = false // 关闭对话框
                            },
                            headlineContent = { Text("奖牌 ${index + 1}") } // 显示奖牌名称
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
    CreatePostScreen(onNavigateBack = {})
}