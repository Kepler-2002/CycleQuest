package com.cyclequest.data.local.preferences

import android.content.Context
import com.cyclequest.data.local.entity.UserStatus
import com.cyclequest.domain.model.User
import javax.inject.Inject

class UserPreferences @Inject constructor(
    private val context: Context
) {
    private val sharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    fun saveUser(user: User) {
        sharedPreferences.edit().apply {
            putString(KEY_USER_ID, user.id)
            putString(KEY_USERNAME, user.username)
            putString(KEY_EMAIL, user.email)
            putString(KEY_PASSWORD, user.password)
            apply()
        }
    }

    fun getUser(): User? {
        val userId = sharedPreferences.getString(KEY_USER_ID, null)
        val username = sharedPreferences.getString(KEY_USERNAME, null)
        val email = sharedPreferences.getString(KEY_EMAIL, null)
        val password = sharedPreferences.getString(KEY_PASSWORD, null)

        return if (userId != null && username != null) {
            User(
                id = userId,
                username = username,
                email = email ?: "",
                password = password ?: "",
                phoneNumber = null,
                avatarUrl = null,
                status = UserStatus.ACTIVE,
                totalRides = 0,
                totalDistance = 0f,
                totalRideTime = 0L,
                lastLoginAt = System.currentTimeMillis(),
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
        } else null
    }

    // 用户登出时清除保存值
    fun clearUser() {
        sharedPreferences.edit().apply {
            remove(KEY_USER_ID)
            remove(KEY_USERNAME)
            remove(KEY_EMAIL)
            remove(KEY_PASSWORD)
            apply()
        }
    }

    companion object {
        private const val PREFS_NAME = "user_prefs"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USERNAME = "username"
        private const val KEY_EMAIL = "email"
        private const val KEY_PASSWORD = "password"
    }
}