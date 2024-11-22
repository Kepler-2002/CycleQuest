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

    fun handleSubmit() {
        // 创建一个 Post 对象
        val post = Post(
            postId = "", // 这里可以留空，数据库会自动生成
            userId = viewModel.userPreferences.getUser()?.id ?: "", // 获取当前用户 ID
            title = "帖子标题", // 你可以根据需要设置标题
            content = postText,
            viewCount = 0,
            likeCount = 0,
            commentCount = 0,
            isTop = false,
            status = PostStatus.NORMAL, // 根据需要设置状态
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )

        // 调用 ViewModel 的 savePost 方法
        viewModel.savePost(post)

        // 提交后清空输入框并返回
        postText = ""
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

//            // Image Selection Box
//            Box(
//                modifier = Modifier
//                    .size(100.dp)
//                    .background(Color.DarkGray.copy(alpha = 0.3f))
//                    .clickable {
//                        // TODO: 实现图片选择逻辑
//                        // 例如，使用图片选择器并更新 selectedImagePath
//                    },
//                contentAlignment = Alignment.Center
//            ) {
////                selectedImagePath?.let {
////                    Image(painter = painterResource(it), contentDescription = "Selected Image", contentScale = ContentScale.Crop)
////                } ?: run {
////                    Icon(
////                        Icons.Default.Add,
////                        contentDescription = "Add Image",
////                        tint = Color.White,
////                        modifier = Modifier.size(32.dp)
////                    )
////                }
//            }

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
            title = { Text("提示") },
            text = { Text("请输入文本") },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("确定")
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