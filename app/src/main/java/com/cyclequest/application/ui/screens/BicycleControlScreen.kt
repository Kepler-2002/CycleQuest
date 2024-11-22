package com.cyclequest.application.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cyclequest.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.cyclequest.application.viewmodels.RoutingViewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun BicycleControlScreen(
    userId: String,
    viewModel: RoutingViewModel = hiltViewModel()
) {
    val routeStates by viewModel.latestRouteStats.collectAsState()

    LaunchedEffect(userId) {
        viewModel.observeLatestRoute(userId)
    }
    Column(modifier = Modifier.fillMaxSize()) {
        // 顶部蓝牙状态栏
        var isBluetoothConnected by remember { mutableStateOf(true) }
        val customLightBlue = Color(0xFF87CEEB)

        Card(modifier = Modifier.background(Color.Transparent)
            .fillMaxWidth()
            .height(50.dp)
        ) {
            Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                // 文本同步蓝牙按钮状态--蓝牙连接功能尚未实现
                Text(if (isBluetoothConnected) "蓝牙已连接" else "蓝牙断开") // 根据状态显示不同文本

                // 填充空白区域
                Spacer(modifier = Modifier.weight(1f))

                // 右侧IconButton部分
                IconButton(
                    /*添加点击逻辑-连接蓝牙*/
                    onClick = { isBluetoothConnected = !isBluetoothConnected }, // 点击切换状态
                    modifier = Modifier.size(40.dp)
                        .background(
                            color = if (isBluetoothConnected) customLightBlue else Color.Transparent,
                            shape = CircleShape // 使用圆形背景
                        )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.bluetooth),
                        contentDescription = "Bluetooth Icon",
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }

        // 显示单车图像的 Card
        Card(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)) {
            // 新增的车辆型号--需要通过设置页面进行输入
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Card(
                    modifier = Modifier
                        .padding(8.dp) // 添加一些内边距
                        .wrapContentSize(), // 自适应内容大小
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // 添加阴影效果
                ) {
                    Text("UCC-Dynamite 2.0", modifier = Modifier.padding(8.dp)) // 显示车辆型号
                }

                Image(
                    painter = painterResource(id = R.drawable.bicycle),
                    contentDescription = "车辆图像",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // 状态信息区域
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("骑行里程", style = MaterialTheme.typography.bodyLarge)
                Text("${routeStates.first} km", style = MaterialTheme.typography.headlineLarge)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("骑行时长", style = MaterialTheme.typography.bodyLarge)
                Text("${routeStates.second} min", style = MaterialTheme.typography.headlineLarge)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("灯光电量", style = MaterialTheme.typography.bodyLarge)
                Text("63 %", style = MaterialTheme.typography.headlineLarge)
            }
        }

        // 底部操作按钮
        // 16.dp调整按钮padding范围大小
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
            val customLightBlue = Color(0xFF87CEEB) // 浅蓝色
            val customYellow = Color(0xFFFFFF4D) // 灯光的黄色光
            //val customTransparent = Color(0x00000000)  透明色


            var isLocked by remember { mutableStateOf(false) }
            IconButton(
                onClick = { isLocked = !isLocked },
                //55.dp调整按钮范围，包裹icon
                modifier = Modifier.size(55.dp)
                    .background(
                        color = if (isLocked) customLightBlue else Color.Transparent,
                        shape = CircleShape // 使用圆形背景
                    )

            ) {
                //31 35.dp调整载入图标的大小.车锁图标略大故使用31dp
                Icon(
                    painter = painterResource(id = R.drawable.lock),
                    contentDescription = "锁车",
                    modifier = Modifier.size(31.dp)
                )
            }

            var isLightOpen by remember { mutableStateOf(false) }
            IconButton(
                onClick = { isLightOpen = !isLightOpen },
                modifier = Modifier.size(55.dp)
                    .background(
                        color = if (isLightOpen) customYellow else Color.Transparent,
                        shape = CircleShape // 使用圆形背景
                    )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.light),
                    contentDescription = "灯光",
                    modifier = Modifier.size(35.dp)
                )
            }


            var isCarFound by remember { mutableStateOf(false) }
            IconButton(
                onClick = { isCarFound = !isCarFound },

                modifier = Modifier.size(55.dp)
                    .background(
                        color = if (isCarFound) customLightBlue else Color.Transparent,
                        shape = CircleShape // 使用圆形背景
                    )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.find_car_icon),
                    contentDescription = "寻车",
                    modifier = Modifier.size(35.dp)
                )
            }
        }
    }
}

