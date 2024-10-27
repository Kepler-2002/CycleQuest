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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

@Composable
fun BicycleControlScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        // 顶部蓝牙状态栏
        var isBluetoothConnected by remember { mutableStateOf(true) }
        val customLightBlue = Color(0xFF87CEEB)

        Card(modifier = Modifier.background(Color.Transparent)
            .fillMaxWidth()
            .height(50.dp)
        ) {
            Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                // 文本需要同步蓝牙情况
                Text("蓝牙已连接")

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
            Image(
                painter = painterResource(id = R.drawable.lexus),
                contentDescription = "车辆图像",
                modifier = Modifier.fillMaxSize()
            )
        }

        // 状态信息区域
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("电量", style = MaterialTheme.typography.bodyLarge)
                Text("63%", style = MaterialTheme.typography.headlineLarge)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("续航", style = MaterialTheme.typography.bodyLarge)
                Text("284 km", style = MaterialTheme.typography.headlineLarge)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("温度", style = MaterialTheme.typography.bodyLarge)
                Text("22°C", style = MaterialTheme.typography.headlineLarge)
            }
        }

        // 底部操作按钮
        // 16.dp调整按钮padding范围大小
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
            val customLightBlue = Color(0xFF87CEEB) // 例如，浅蓝色
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

            var isWindowOpen by remember { mutableStateOf(false) }
            IconButton(
                onClick = { isWindowOpen = !isWindowOpen },
                modifier = Modifier.size(55.dp)
                    .background(
                        color = if (isWindowOpen) customLightBlue else Color.Transparent,
                        shape = CircleShape // 使用圆形背景
                    )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.window),
                    contentDescription = "开窗",
                    modifier = Modifier.size(35.dp)
                )
            }

            var isTrunkOpen by remember { mutableStateOf(false) }
            IconButton(
                onClick = { isTrunkOpen = !isTrunkOpen },
                modifier = Modifier.size(55.dp)
                    .background(
                        color = if (isTrunkOpen) customLightBlue else Color.Transparent,
                        shape = CircleShape // 使用圆形背景
                    )

            ) {
                Icon(
                    painter = painterResource(id = R.drawable.trunk_icon),
                    contentDescription = "后备箱",
                    modifier = Modifier.size(35.dp))
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

