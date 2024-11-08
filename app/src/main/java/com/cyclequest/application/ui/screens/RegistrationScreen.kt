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
import androidx.navigation.NavController
import com.cyclequest.application.viewmodels.RegistrationViewModel
import com.cyclequest.core.network.ApiError
import com.cyclequest.core.network.ApiResult

@Composable
fun RegistrationScreen(
    navController: NavController,
    registrationViewModel: RegistrationViewModel
) {
    // 获取状态
    val username by registrationViewModel.username.observeAsState("")
    val email by registrationViewModel.email.observeAsState("")
    val password by registrationViewModel.password.observeAsState("")
    val registrationResult by registrationViewModel.registrationResult.observeAsState(ApiResult.Loading)
    var error by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 顶部栏
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "返回")
            }
            Text("注册", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.width(48.dp))
        }

        // 输入字段
        RegistrationInputFields(
            username = username,
            email = email,
            password = password,
            onUsernameChange = { registrationViewModel.username.value = it },
            onEmailChange = { registrationViewModel.email.value = it },
            onPasswordChange = { registrationViewModel.password.value = it }
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
                    if (registrationViewModel.isInputValid()) {
                        registrationViewModel.userRegisterLocally()
                    } else {
                        error = "请填写所有字段"
                    }
                }
            ) {
                Text("本地注册")
            }

            Button(
                onClick = {
                    if (password.length < 8) {
                        error = "密码必须至少为8个字符"
                    } else {
                        error = ""
                        // TODO: 处理下一步逻辑
                    }
                }
            ) {
                Text("下一步")
            }
        }

        // 注册结果处理
        RegistrationResultHandler(
            registrationResult = registrationResult,
            onSuccess = { navController.navigateUp() }
        )
    }
}

@Composable
private fun RegistrationInputFields(
    username: String,
    email: String,
    password: String,
    onUsernameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit
) {
    TextField(
        value = username,
        onValueChange = onUsernameChange,
        label = { Text("用户名") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )

    Spacer(modifier = Modifier.height(16.dp))

    TextField(
        value = email,
        onValueChange = onEmailChange,
        label = { Text("电子邮箱") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )

    Spacer(modifier = Modifier.height(16.dp))

    TextField(
        value = password,
        onValueChange = onPasswordChange,
        label = { Text("密码") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        visualTransformation = PasswordVisualTransformation()
    )
}

@Composable
private fun RegistrationResultHandler(
    registrationResult: ApiResult<*>,
    onSuccess: () -> Unit
) {
    when (registrationResult) {
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
            LaunchedEffect(Unit) {
                onSuccess()
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