package com.example.amap.core.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

object PermissionUtil {
    fun isPermissionGranted(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    fun arePermissionsGranted(context: Context, permissions: Array<String>): Boolean {
        return permissions.all { isPermissionGranted(context, it) }
    }
}

