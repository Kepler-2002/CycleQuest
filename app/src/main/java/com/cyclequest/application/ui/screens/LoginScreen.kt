package com.cyclequest.application.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cyclequest.application.viewmodels.RegistrationViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.collectAsState
import com.cyclequest.core.network.ApiResult
import com.cyclequest.domain.model.User
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon


@Composable
fun LoginScreen(
    registrationviewModel: RegistrationViewModel,
    navController: NavController
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    // 收集登录状态
    val loginState by registrationviewModel.loginState.collectAsState()

    // 根据登录状态更新UI
    LaunchedEffect(loginState) {
        when (loginState) {
            is ApiResult.Success -> {
                val user = (loginState as ApiResult.Success<User>).data
                navController.navigate("settings/${user.id}/${user.username}")
            }
            is ApiResult.Error -> {
                error = (loginState as ApiResult.Error).error.toString()
            }
            else -> { /* 处理其他状态 */ }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 添加顶部导航栏
        IconButton(
            onClick = {
                navController.navigate("register") {
                    // 清除返回栈直到 registration 界面
                    popUpTo("register")
                }
            },
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "返回注册界面"
            )
        }

        Text(
            "用户登录",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("邮箱") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("密码") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        if (error.isNotEmpty()) {
            Text(
                text = error,
                color = Color.Red,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                registrationviewModel.loginUser(
                    email = email,
                    password = password,
                    onSuccess = { /* 成功回调已在 LaunchedEffect 中处理 */ },
                    onError = { error = it }
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("登录")
        }
    }
}