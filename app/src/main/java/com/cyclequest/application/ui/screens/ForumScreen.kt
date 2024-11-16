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
import com.cyclequest.application.viewmodels.ForumViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForumScreen(navController: NavController, viewModel: ForumViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {

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
                onClick = { navController.navigate("CreatePostScreen")  },
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
                items(5) { index ->
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
                1 -> {
                    // My posts content
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(3) { index ->
                            PostItem(index)
                        }
                    }
                }
            }
        }
    }
}


