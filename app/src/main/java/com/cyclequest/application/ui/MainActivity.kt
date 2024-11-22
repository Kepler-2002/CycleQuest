package com.cyclequest.application.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cyclequest.application.ui.screens.*
import com.cyclequest.ui.theme.AMapTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.createBitmap
import okhttp3.OkHttpClient
import org.greenrobot.eventbus.EventBus
import org.slf4j.LoggerFactory
import com.cyclequest.R

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val logger = LoggerFactory.getLogger(MainActivity::class.java)
    private val okHttpClient = OkHttpClient()
    private val eventBus = EventBus.getDefault()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logger.info("MainActivity created")
        setContent {
            AMapTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf("单车控制", "地图", "论坛", "设置")

    // 修改图标列表
    val icons = listOf(
        painterResource(id = R.drawable.bicycle_icon),
        painterResource(id = R.drawable.place_icon),
        painterResource(id = R.drawable.info_icon),
        painterResource(id = R.drawable.setting_icon),
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(icons[index], contentDescription = item, modifier = Modifier.size(24.dp)) },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            navController.navigate(item)
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = "单车控制", Modifier.padding(innerPadding)) {
            composable("单车控制") { BicycleControlScreen() }
            composable("地图") { MapScreen() }
            composable("论坛") { ForumScreen(navController) }
            composable("设置") { SettingsScreen(navController) }
            composable("CreatePostScreen") { CreatePostScreen(onNavigateBack = { navController.popBackStack() }) }
        }
    }
}

