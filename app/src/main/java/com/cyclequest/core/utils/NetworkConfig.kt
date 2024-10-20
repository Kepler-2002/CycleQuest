package com.cyclequest.core.network

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkConfig @Inject constructor() {
    val baseUrl = "https://api.example.com/" // 替换为实际的API基础URL
    val connectTimeout = 30L // 连接超时时间（秒）
    val readTimeout = 30L // 读取超时时间（秒）
    val writeTimeout = 30L // 写入超时时间（秒）
}

