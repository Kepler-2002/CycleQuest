package com.cyclequest.application.ui.screens

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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.ui.text.font.FontWeight


@Composable
fun ProfileScreen() {
    //card视图展示用户当前头像 按钮文本：修改头像--功能：上传图片到？
    //列表视图 列表项：用户ID 用户名 性别 出生日期
    //列表项整体是一个按钮，可以点击
    //用户ID 左侧文本显示框，右侧显示用户ID--不可更改--功能：用户注册
    //昵称 左侧文本框，右侧通过dialog？浮窗输入文本，有确定按钮
    //性别
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Card视图用于展示头像 及按钮：实现修改头像功能
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .weight(1f) // 使用weight将Card放在上半部分
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxHeight() // 确保Row填满高度以实现垂直居中
                    .wrapContentHeight(Alignment.CenterVertically), // 垂直居中
                verticalAlignment = Alignment.CenterVertically
            ) {
                //载入默认用户头像 id name--通过创建User类，设置默认用户

                Image(
                    painter = painterResource(R.drawable.user1_lynn),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                )
                // 用户头像 和 信息的间隔
                Spacer(modifier = Modifier.width(16.dp))
                // 替换为居中的按钮
                Button(
                    onClick = { /* Handle change avatar action */ },
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Text("修改头像")
                }
            }
        }

        // 列表视图显示设置选项
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .weight(2f) // 使用weight将列表放在下半部分
        ) {
            CustomListItem(
                text = "CycleQuestID",
                onClick = {
                    // 弹出浮窗
                    showDialog = true // 假设您有一个状态来控制浮窗的显示
                }
            )
            DashedDivider()

            CustomListItem(
                text = "性别",
                onClick = {
                    showDialog = true
                }
            )
            DashedDivider()

            CustomListItem(
                text = "出生日期",
                onClick = {
                    showDialog = true
                }
            )
            DashedDivider()

            CustomListItem(
                text = "退出登录",
                onClick = {
                    showDialog = true
                }
            )
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false }, // 点击外部区域关闭对话框
                title = { Text(text = "提示") },
                text = { Text("已弹出浮窗") },
                confirmButton = {
                    Button(
                        onClick = { showDialog = false } // 点击确认按钮关闭对话框
                    ) {
                        Text("确定")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showDialog = false } // 点击取消按钮关闭对话框
                    ) {
                        Text("取消")
                    }
                }
            )
        }
    }
}


@Composable
fun CustomListItem(
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick) // 整个区域可点击
            .padding(16.dp), // 添加内边距
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = text, fontWeight = FontWeight.Bold) // 加粗文本
        Text(text = "点击这里") // 右侧文本，点击后弹出浮窗
    }
}
