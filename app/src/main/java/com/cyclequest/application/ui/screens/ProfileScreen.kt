package com.cyclequest.application.ui.screens
import com.cyclequest.application.ui.component.setting.CustomAlertDialog
import com.cyclequest.application.ui.component.setting.CustomListItem
import androidx.compose.ui.layout.ContentScale
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.IconButton
import androidx.lifecycle.ViewModel
import com.cyclequest.application.ui.component.setting.ProfileViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cyclequest.application.ui.component.setting.GenderSelectionDialog
import com.cyclequest.application.ui.component.setting.InputTextDialog
import com.cyclequest.application.ui.component.setting.DashedDivider

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = viewModel()) {
    /*2024-10-28 13：46记录
    完成了用户ID列表项UI重建，未实现复制功能--bug
    完成了性别选择器的浮窗实现，未实现向数据库传送数据？
    待完成：出生日期浮窗选择---或在用户注册时写入用户数据表？
            但用户需要修改日期时，仍然需要浮窗滚动选择日期
    待完成：浮窗输入车辆型号---写入数据表，车控页text应该从数据表读取
    待完成：点击退出登录，所有恢复默认值---默认值变量已写好

    梳理性别选择器的代码--为什么出现两个性别相关val变量
    */

    /*
    1.需要完全示头像--居中; 修改头像按钮背景色-透明; 头像上传后--到哪里去了？
    2.列表中的CycleQuestID 应当展示文本框 和复制icon
    3.id列表项的点击 -- 弹窗功能1：显示文本--CycleQuestID已复制 --- 访问手机剪贴板
    4.性别列表项点击后 -- 弹窗功能2.1：选择性别
    5.出生日期列表项点击 -- 弹窗功能2.2：滚动选择日期
    6.增加列表项 -- 车辆信息 -- 弹窗功能3：输入文本 -- nav_to_BCScreen_TEXT
    7.退出登录列表项 -- 弹簧功能3：显示文本 -- 已退出登录 -- 修改所有默认信息？

    ps：为设置页进行的数据传递--id 头像 -- ？
    ps：为设置页--安全隐私列表项设置弹窗来阅读条款--文本滚动
    */
    var showCopyDialog by remember { mutableStateOf(false) }
    var cycleQuestID by remember { mutableStateOf("001001001") }
    var gender by remember { mutableStateOf("男") }
    var birthDate by remember { mutableStateOf("选择出生日期") }
    //此处默认值相当于文本框提示了
    var bicycleType by remember { mutableStateOf(value = "每人都可以有的哈啰单车") }
    var showGenderDialog by remember { mutableStateOf(false) }
    var showDateDialog by remember { mutableStateOf(false) }
    var showInputDialog by remember { mutableStateOf(false) }
    //var inputText by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf(gender) } // 用于存储选择的性别
    var showQuitDialog by remember { mutableStateOf(false) }
    val profileViewModel: ProfileViewModel = viewModel()
    val copyResult = profileViewModel.copyToClipboard(cycleQuestID)
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
                    .padding(30.dp)
                    .fillMaxHeight() // 确保Row填满高度以实现垂直居中
                    .wrapContentHeight(Alignment.CenterVertically), // 垂直居中
                verticalAlignment = Alignment.CenterVertically
            ) {
                //载入默认用户头像 id name--通过创建User类，设置默认用户

                Image(
                    painter = painterResource(R.drawable.user1_lynn),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop // 添加此行以填满CircleShape
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


            //需要实现复制功能--列表项可点击，复制icon也可点击
            //需要区分或抛弃一个
            CustomListItem(
                text = "CycleQuestID",
                //需要从数据库读取id
                rightText = cycleQuestID,
                onClick = {
                    //显示浮窗提示复制成功，但复制的功能连接在icon上？
                    //除非@composable必须用-必须使用icon的onclick，
                    // 就可以取消icon的点击效果,onclick能否定义两个component？
                    // showCopyDialog = true
                },

                // 需要通过点击实现复制
                rightIcon = {
                    IconButton(onClick = {
                        //创建ProfileViewModel实例,
                        //通过copyResult存储 调用ViewModel.copyToClipboard方法 获取的剪贴板值
                        copyResult
                    //因下面这句必须直接在@composable注释的函数下直接使用
                    //profileViewModel.copyToClipboard(cycleQuestID)
                        showCopyDialog = true
                    }) {
                        Icon(
                            painterResource(R.drawable.copy_icon),
                            modifier = Modifier.padding(0.dp),
                            contentDescription = "Copy"
                        )
                    }
                }
            )
            DashedDivider()

            CustomListItem(
                text = "性别",
                rightText = gender,
                onClick = {
                    showGenderDialog = true
                }
            )
            DashedDivider()

            CustomListItem(
                text = "出生日期",
                rightText = birthDate,
                onClick = {
                    showDateDialog = true
                }
            )
            DashedDivider()

            CustomListItem(
                text = "单车型号",
                //bicycleType默认值就是文本框提示
                rightText = bicycleType,
                onClick = {
                    showInputDialog = true
                }
            )
            DashedDivider()

            CustomListItem(
                text = "退出登录",
                rightText = "点击退出",
                onClick = {
                    showQuitDialog = true
                }
            )
        }

        //第一项列表项的 -- 浮窗提示
        if (showCopyDialog) {
            AlertDialog(
                onDismissRequest = { showCopyDialog = false }, // 点击外部区域关闭对话框
                title = { Text(text = "提示") },
                text = { Text("已复制CycleQuestID") },
                confirmButton = {
                    Button(
                        onClick = { showCopyDialog = false } // 点击确认按钮关闭对话框
                    ) {
                        Text("确定")
                    }
                },
                //此处的取消就没什么实际意义，也不会删除剪贴板内容
                dismissButton = {
                    Button(
                        onClick = { showCopyDialog = false } // 点击取消按钮关闭对话框
                    ) {
                        Text("取消")
                    }
                }
            )
        }

        // 性别选择器1
        if (showGenderDialog) {
            AlertDialog(
                onDismissRequest = { showGenderDialog = false },
                title = { Text("选择性别") },
                text = {
                    Column {
                        // 假设你有一个性别列表
                        val genders = listOf("男", "女", "其他")
                        LazyColumn {
                            items(genders) { genderOption ->
                                Text(
                                    text = genderOption,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            selectedGender = genderOption // 更新选择的性别
                                            gender = genderOption // 更新主界面的性别
                                            showGenderDialog = false // 关闭对话框
                                        }
                                        .padding(16.dp)
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(onClick = { showGenderDialog = false }) {
                        Text("确认")
                    }
                },
                dismissButton = {
                    Button(onClick = { showGenderDialog = false }) {
                        Text("取消")
                    }
                }
            )
        }


        // Date selection dialog---未完成
        if (showDateDialog) {
            // Implement date selection dialog
        }

        //第四项列表项的输入功能
        if (showInputDialog){
            //实现调用输入文本的功能
            //要实现inputText值在浮窗外更新到 bicycleType
            // 1.要确保bicycleType默认值为空，
            // 2.使用此处新定义的bicycleType 而不是viewModel.bicycleType
            InputTextDialog(
                initialText = bicycleType, // 使用当前的bicycleType作为初始文本
                onConfirm = { inputText ->
                    bicycleType = inputText // 更新bicycleType
                    showInputDialog = false // 关闭对话框
                },
                onDismiss = { showInputDialog = false } // 关闭对话框
            )
        }

        // UserQuit dialog -- 展示信息 恢复默认值？ ---未完成
        if (showQuitDialog) {
            // Implement date selection dialog
        }

    }




}


//--------