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
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cyclequest.application.ui.component.setting.ListItem
import com.cyclequest.application.ui.component.setting.DashedDivider
import com.cyclequest.data.local.entity.UserStatus
import com.cyclequest.domain.model.User


@Composable
fun SettingsScreen(
    navController: NavController,
    userId: String? = null,
    username: String? = null,
    email: String? = null,
    password: String? = null
) {
    if (userId == null || username == null) {
        // 如果必要参数为空，重定向到登录页面
        LaunchedEffect(Unit) {
            navController.navigate("login") {
                popUpTo("settings") { inclusive = true }
            }
        }
        return
    }

    //使用传入的用户信息更新界面
    val user = User(
        userId ?: "",
        username ?: "",
        email = email ?: "",
        password = password ?: "",
        phoneNumber = "12312341234",
        avatarUrl = "url",
        status = UserStatus.ACTIVE,
        totalRides = 2,
        totalDistance = 1111.11F,
        totalRideTime = 516,
        lastLoginAt = System.currentTimeMillis(),
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis(),

    )

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
                    Text(user.username, style = MaterialTheme.typography.bodyMedium)
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
                    // 导航到 profileScreen的页面
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

            DashedDivider()

            ListItem(
                text = "注册新用户",
                onClick = { // 跳转到用户注册页面
                    navController.navigate("register")
                }
            )

        }

    }
}

/*data class User (val id: String, val name: String) {
    //记得添加用户注册-存储到数据库？
    //无用户时，使用默认用户
}
val user = User("001001001", "Lynn" )*/

