package com.cyclequest.application.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.cyclequest.application.viewmodels.RegistrationViewModel
import com.cyclequest.core.network.ApiError
import com.cyclequest.core.network.ApiResult
import com.cyclequest.domain.model.User

@Composable
fun RegistrationScreen(
    navController: NavController,
    registrationViewModel: RegistrationViewModel
) {
    // 获取状态
    val username by registrationViewModel.username.observeAsState("")
    val email by registrationViewModel.email.observeAsState("")
    val password by registrationViewModel.password.observeAsState("")
    //注册结果响应 registrationResult的状态 -- 刚进入界面还没有开始时，用户状态不应为loading。使用null
    val registrationResult by registrationViewModel.registrationResult.observeAsState(null)
    var error by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 顶部栏
        Text(
            "用户注册",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // 输入字段
        RegistrationInputFields(
            username = username,
            email = email,
            password = password,
            onUsernameChange = { registrationViewModel.username.value = it },
            onEmailChange = { registrationViewModel.email.value = it },
            onPasswordChange = { registrationViewModel.password.value = it },
            registrationViewModel = registrationViewModel // 传入 ViewModel
        )

        // 错误显示
        if (error.isNotEmpty()) {
            Text(
                text = error,
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 按钮行
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    registrationViewModel.userRegisterLocally()
                    //注册成功后切换登录模块
                    // bug: 点击 本地注册 注册成功后，应自动跳转login，但是并没有实现
                    // -- 因为 registrationResult 的状态更新是异步的
                    // 移除 点击按钮立即导航的逻辑
                    /*if (registrationResult is ApiResult.Success){
                        navController.navigate("login")
                    }*/
                    error = ""  // 清除之前提示的错误信息
                },
                // enabled = username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()
            ) {
                Text("本地注册")
            }

            Button(
                onClick = {
                    registrationViewModel.skipRegistration()
                    navController.navigate("单车控制"){
                        popUpTo("register") {inclusive = true}
                    }
                    error = "" // 清除之前的错误信息
                }
            ) {
                Text("跳过注册")
                // 跳过注册 -- 则使用默认用户信息 + 跳转车控页面
            }
        }

        // 注册结果处理
        RegistrationResultHandler(
            registrationResult = registrationResult,
            navController = navController,
            onSuccess = { navController.navigateUp() }
            // 暂时安排注册成功后跳转回 路由控制的页面 -- 后续将注册放到首页，路由跳转车控页
        )

        // 添加登录按钮
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(
                onClick = { navController.navigate("login") }
            ) {
                Text("已有账号？点击登录")
            }
        }

    }
}

@Composable
private fun RegistrationInputFields(
    username: String,
    email: String,
    password: String,
    onUsernameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    registrationViewModel: RegistrationViewModel
) {
    // 初始化文本框错误状态的默认值
    var emailError by remember { mutableStateOf<String?>(null) }
    var usernameError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    TextField(
        value = username,
        onValueChange = {
            onUsernameChange(it)
            usernameError = registrationViewModel.validateUsername(it)
        },
        label = { Text("用户名") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        isError = usernameError != null,
        supportingText = {
            usernameError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

    )
    Spacer(modifier = Modifier.height(16.dp))
    TextField(
        value = email,
        onValueChange = {
            onEmailChange(it)
            emailError = registrationViewModel.validateEmail(it)
        },
        label = { Text("电子邮箱") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        isError = emailError != null,
        supportingText = {
            emailError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    )
    Spacer(modifier = Modifier.height(16.dp))
    TextField(
        value = password,
        onValueChange = {
            onPasswordChange(it)
            passwordError = registrationViewModel.validatePassword(it)
        },
        label = { Text("密码") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        isError = passwordError != null,
        supportingText = {
            passwordError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    )
}

@Composable
private fun RegistrationResultHandler(
    registrationResult: ApiResult<User>?,
    navController: NavController,
    onSuccess: () -> Unit
) {
    when (registrationResult) {
        // Bug: loading状态 的加载圆圈 一直显示
        // 检查viewModel -- registrationResult 默认值 被设置为 loading
        null -> {
            // 初始状态不显示内容
        }
        is ApiResult.Loading -> {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        is ApiResult.Success -> {
            // 在 RegistrationResultHandler 中的 Success 分支添加导航逻辑
            // 使用 LaunchedEffect 并将 registrationResult 作为 key，确保在注册成功时触发导航
            // 添加了 popUpTo 以清除回退栈中的注册页面
            LaunchedEffect(registrationResult) {
                navController.navigate("login") {
                    popUpTo("register") { inclusive = true }
                }
            }
        }
        is ApiResult.Error -> {
            val errorMessage = when (val error = registrationResult.error) {
                is ApiError.HttpError -> "网络错误: ${error.code} - ${error.message}"
                is ApiError.NetworkError -> "连接错误: ${error.throwable.message}"
                is ApiError.ServerError -> "服务器错误: ${error.message}"
            }
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

/* navigation 传递用户参数时的使用方式
NavHost(navController = navController, startDestination = "first_screen") {
    composable("first_screen") {
        FirstScreen(navController)
    }
    composable("second_screen/{userId}") { backStackEntry ->
        val userId = backStackEntry.arguments?.getString("userId") ?: "unknown"
        SecondScreen(userId)
    }
}
*/