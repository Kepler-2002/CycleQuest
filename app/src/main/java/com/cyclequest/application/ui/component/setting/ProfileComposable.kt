package com.cyclequest.application.ui.component.setting

import androidx.appcompat.app.AlertDialog
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.ViewModel
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextField
import androidx.lifecycle.viewmodel.compose.viewModel


// 此处定义的profileViewModel，需要调试current性质
// 先将车辆型号存储在viewModel中
class ProfileViewModel : ViewModel() {
    var bicycleType by mutableStateOf("请输入车辆型号") // 添加此行以创建 bicycleType 属性

    @Composable
    fun getClipboardManager() = LocalClipboardManager.current // 将 current 包裹在 @Composable 注释的函数中

    @Composable
    fun copyToClipboard(text: String) {
        val profileViewModel: ProfileViewModel = viewModel()
        getClipboardManager().setText(AnnotatedString(text)) // 使用新的函数获取 clipboardManager
    }
}

// 列表项组件的定义内容
// 每一个列表项的点击都不能是@composable，故profileScreen中用if状态值来调用component
//Icon按钮的点击呢？--可以使用composable
@Composable
fun CustomListItem(
    text: String,
    rightText: String,
    onClick: () -> Unit,
    rightIcon: @Composable (() -> Unit)? = null
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(15.dp),
        horizontalArrangement = Arrangement.SpaceBetween, // 两端对齐
        verticalAlignment = Alignment.CenterVertically// 垂直对齐到中心
    ) {
        Text(text = text)
        Row (verticalAlignment = Alignment.CenterVertically){
            Text(text = rightText)
            Spacer(modifier = Modifier.width(5.dp))
            rightIcon?.invoke() // Display the right icon if provided
        }
    }
}

//五个列表项每个功能都不一样，通用的浮窗模板先放在这里
@Composable
fun CustomAlertDialog( // 重命名为 CustomAlertDialog
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



// 性别选择器1
@Composable
fun GenderSelectionDialog(onConfirm: (String) -> Unit, onDismiss: () -> Unit) {
    val genders = listOf("男", "女", "其他") // 性别选项
    var selectedGender by remember { mutableStateOf(genders[0]) } // 默认选择第一个性别
// 组件名有点乱
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("选择性别") },
        text = {
            Column {
                LazyColumn {
                    items(genders) { gender ->
                        Text(
                            text = gender,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedGender = gender
                                }
                                .padding(16.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onConfirm(selectedGender) // 确认选择
                onDismiss() // 关闭对话框
            }) {
                Text("确认")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}



// Implement the dialog for date selection
@Composable
fun DateSelectionDialog(onConfirm: (String) -> Unit, onDismiss: () -> Unit) {
    // Implement the dialog UI for selecting date
}


// 输入文本的浮窗 -- 注意inputText生命周期
@Composable
fun InputTextDialog(
    initialText: String = "",
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var inputText by remember { mutableStateOf(initialText) } // 用于保存输入的文本

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("请输入您的车辆型号") },
        text = {
            Column {
                TextField(
                    value = inputText,
                    onValueChange = { inputText = it }, // 更新输入文本
                    label = { Text("请输入内容") } // 输入框的标签
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                //viewModel.bicycleType = inputText // 此句无法传递值
                onConfirm(inputText) // 确认时传递输入的文本
                onDismiss() // 关闭对话框
            }) {
                Text("确定")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}
