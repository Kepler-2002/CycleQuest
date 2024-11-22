package com.cyclequest.application.ui

import android.os.Bundle
import android.provider.ContactsContract.Profile
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import okhttp3.OkHttpClient
import org.greenrobot.eventbus.EventBus
import org.slf4j.LoggerFactory
import com.cyclequest.R
import com.cyclequest.application.ui.component.setting.ProfileViewModel
import com.cyclequest.application.viewmodels.RegistrationModule
import com.cyclequest.application.viewmodels.RegistrationViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val logger = LoggerFactory.getLogger(MainActivity::class.java)
    private val eventBus = EventBus.getDefault()

    // 在MainActivity中，获取viewModels实例
    // private val registrationViewModel: RegistrationViewModel by viewModels()

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
    val items = listOf("单车控制", "地图", "论坛", "settings")
    val registrationViewModel: RegistrationViewModel = viewModel()

    // 收集用户状态
    val currentUser by registrationViewModel.currentUser.collectAsState()

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
                            when (item) {
                                "settings" -> {
                                    currentUser?.let { user ->
                                        navController.navigate("settings/${user.id}/${user.username}")
                                    } ?: run {
                                        navController.navigate("login")
                                    }
                                }
                                else -> {
                                    navController.navigate(item) {
                                        popUpTo(navController.graph.startDestinationId)
                                        launchSingleTop = true
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = "register", Modifier.padding(innerPadding)) {
            composable("register") {
                RegistrationScreen(
                    navController = navController,
                    registrationViewModel = registrationViewModel
                )
            }
            composable("单车控制") { BicycleControlScreen() }
            composable("地图") { MapScreen() }
            composable("论坛") { ForumScreen() }

            composable("profile") {
                ProfileScreen(
                    navController = navController,
                    registrationViewModel = registrationViewModel
                    // profileViewModel = profileViewModel
                )
            }
            composable("login") {
                LoginScreen(
                    navController = navController,
                    registrationviewModel = registrationViewModel
                )
            }

            composable(
                "settings/{userId}/{username}",
                arguments  = listOf(
                    navArgument("userId") { type = NavType.StringType},
                    navArgument("username") { type = NavType.StringType}
                )
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getString("userId")
                val username = backStackEntry.arguments?.getString("username")
                val email = backStackEntry.arguments?.getString("email")
                val password = backStackEntry.arguments?.getString("password")
                SettingsScreen(
                    navController = navController,
                    userId = userId,
                    username = username,
                    email = email,
                    password = password
                )
            }
        }
    }
}
