package com.cyclequest.application.ui.screens
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.cyclequest.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun SettingsScreen() {
    val navController = rememberNavController() // 创建导航控制器

    NavHost(navController = navController, startDestination = "settings") {
        composable("settings") {
            // 这里是设置页面的内容
            Column {
                // Card视图展示用户信息
                Card(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        //载入默认用户头像 id name--通过创建User类，设置默认用户
                        /*设计 数据库中注册用户的调用
                               用户注册页
                        */
                        Image(
                            painter = painterResource(R.drawable.user1_lynn),
                            contentDescription = null,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                        )
                        // 用户头像 和 信息的间隔
                        Spacer(modifier = Modifier.width(16.dp))
                        // id name 垂直排列
                        Column {
                            Text(user.id, style = MaterialTheme.typography.titleMedium)
                            Text(user.name, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }

                // 列表视图显示设置选项
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    ListItem(
                        text = "个人资料",
                        onClick = {
                            // 跳转到个人资料页面
                            navController.navigate("profile")
                        }
                    )
                    //列表项分隔符
                    DashedDivider()

                    ListItem(
                        text = "安全隐私",
                        onClick = { /* Handle click for 安全隐私 */ }
                    )

                    DashedDivider()

                    ListItem(
                        text = "通知设置",
                        onClick = { /* Handle click for 通知设置 */ }
                    )

                    DashedDivider()

                    ListItem(
                        text = "帮助与反馈",
                        onClick = { /* Handle click for 帮助与反馈 */ }
                    )
                }
            }
        }
        composable("profile") { ProfileScreen() } // 添加个人资料页面的路由
    }
}

data class User (val id: String, val name: String) {
    //记得添加用户注册-存储到数据库？
    //无用户时，使用默认用户
}
val user = User("001001001", "Lynn" )

//创建Dashed Divider方法 用于分隔符的频繁调用
@Composable
fun DashedDivider() {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
    ) {
        val dashWidth = 10f
        val dashGap = 5f
        var x = 0f
        while (x < size.width) {
            drawLine(
                color = Color.Gray,
                start = Offset(x, 0f),
                end = Offset(x + dashWidth, 0f),
                strokeWidth = 1f
            )
            x += dashWidth + dashGap
        }
    }
}

// 创建ListItem using Material3
@Composable
fun ListItem(text: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(8.dp), // Adjust padding as needed
        //将方法从elevation改为cardElevation
        elevation = CardDefaults.cardElevation(4.dp) // Optional elevation
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), // Padding inside the card
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = text, modifier = Modifier.weight(1f)) // Text on the left
            Icon(
                imageVector = Icons.Filled.ArrowForward, // Use your desired arrow icon
                contentDescription = null // Provide a description for accessibility
            )
        }
    }
}

// 设置页；使用了material3库设计规范
// 所有跳转用dialog显示对话框来模拟
// Column_Part1：左上角图标，跳转论坛私信，右上角图标，跳转设置页面
// Column_Part2：Card视图，显示用户信息【头像 id 状态】
// Column_Part3：显示button[收藏路线]
// additional：显示收藏路线的名称，点击可以跳转
// 单纯的设置页，列表视图。点击button应该可以打开dialog
// 创建用户需要存储在数据库中，这里先使用一个全局变量来模拟
