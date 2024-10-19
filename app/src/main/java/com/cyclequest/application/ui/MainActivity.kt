package com.cyclequest.application.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
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
import okhttp3.OkHttpClient
import org.greenrobot.eventbus.EventBus
import org.slf4j.LoggerFactory

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
        Icons.Filled.Home,
        Icons.Filled.Place,
        Icons.Filled.Info,
        Icons.Filled.Settings
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(icons[index], contentDescription = item) },
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
            composable("论坛") { ForumScreen() }
            composable("设置") { SettingsScreen() }
        }
    }
}
