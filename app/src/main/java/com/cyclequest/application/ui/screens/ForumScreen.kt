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
import com.cyclequest.application.viewmodels.ForumViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cyclequest.domain.model.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.cyclequest.domain.repository.AchievementRepository
import com.cyclequest.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForumScreen(navController: NavController, viewModel: ForumViewModel = hiltViewModel()) {

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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    try {
                        navController.navigate("CreatePostScreen")
                    } catch (e: Exception) {
                        // 处理异常，例如记录日志
                        Log.e("ForumScreen", "Navigation error: ${e.message}")
                    }
                },
                modifier = Modifier.size(72.dp),
                containerColor = Color.Green,
                contentColor = Color.White
            ) {
                Text(
                    "+",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
//            // Status tabs
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 16.dp)
//            ) {
//                Text("正在进行", color = Color.Green, fontWeight = FontWeight.Bold)
//                Spacer(modifier = Modifier.width(16.dp))
//                Text("即将上新", color = Color.Gray)
//            }

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
                items(2) { index ->
                    AwardItem(index)
                }
            }

//            // My post space
//            Text(
//                "我的帖子",
//                modifier = Modifier.padding(16.dp),
//                style = MaterialTheme.typography.titleMedium
//            )
//
//            // Posts row
//            LazyRow(
//                modifier = Modifier.padding(horizontal = 16.dp),
//                horizontalArrangement = Arrangement.spacedBy(16.dp)
//            ) {
//                items(3) { index ->
//                    PostItem(index)
//                }
//            }

            // Category tabs
            val selectedTabIndex by viewModel.selectedTabIndex.collectAsState()

            TabRow(selectedTabIndex = selectedTabIndex) {
                listOf("推荐", "我的帖子").forEachIndexed { index, title ->
                    Tab(
                        selected = index == selectedTabIndex,
                        onClick = { viewModel.selectTab(index) },
                        text = { Text(title) }
                    )
                }
            }

            // Award events
            when (selectedTabIndex) {
                0 -> {
                    // Recommended content
                    val achievements by viewModel.achievements.collectAsState()

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(achievements) { achievement ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
//                                    val iconResId = when (achievement.iconUrl) {
//                                        "achievements/distance_bronze.png" -> R.drawable.distance_bronze
//                                        "achievements/distance_silver.png" -> R.drawable.distance_silver
//                                        "achievements/distance_gold.png" -> R.drawable.distance_gold
//                                        "achievements/explorer_bronze.png" -> R.drawable.explorer_bronze
//                                        "achievements/explorer_gold.png" -> R.drawable.explorer_gold
//                                        else -> R.drawable.default_icon // 默认图标
//                                    }
                                    Image(
                                        painter = painterResource(id = achievement.resourceId), // 使用iconUrl
                                        contentDescription = null,
                                        modifier = Modifier.size(60.dp),
                                        contentScale = ContentScale.Crop
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Column {
                                        Text(text = achievement.name, style = MaterialTheme.typography.titleMedium)
                                        Text(text = achievement.description, style = MaterialTheme.typography.bodySmall)
                                        Text(text = achievement.type.toString(), style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                            }
                        }
                    }
                }
                1 -> {
                    // My posts content
                    val userPosts by viewModel.userPostsFlow.collectAsState(initial = emptyList())

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(userPosts) { postEntity ->
                            val imageResId = when (postEntity.postId.toInt() % 3) { // 使用postId的余数来轮流选择图片
                                0 -> R.drawable.forum1
                                1 -> R.drawable.forum2
                                else -> R.drawable.forum3
                            }
                            PostItem(post = Post(
                                postId = postEntity.postId,
                                userId = postEntity.userId,
                                title = postEntity.title,
                                content = postEntity.content,
                                viewCount = postEntity.viewCount,
                                likeCount = postEntity.likeCount,
                                commentCount = postEntity.commentCount,
                                isTop = postEntity.isTop,
                                status = postEntity.status,
                                createdAt = postEntity.createdAt,
                                updatedAt = postEntity.updatedAt

                            ), imageResId = imageResId)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PostItem(post: Post, imageResId: Int) {
    // 显示帖子内容，例如标题和内容
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp) // 增加每个帖子的间距
    ) {
        Image(
            painter = painterResource(id = imageResId), // 显示帖子图片
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp), // 增大图片高度
            contentScale = ContentScale.Crop
        )
        Text(text = post.title, fontWeight = FontWeight.Bold)
        Text(text = post.content)
    }
}
