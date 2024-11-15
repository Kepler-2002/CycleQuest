package com.cyclequest.application.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyclequest.core.database.sync.DatabaseSync
import com.cyclequest.core.network.ApiError
import com.cyclequest.core.network.ApiResult
import com.cyclequest.data.local.dao.UserDao
import com.cyclequest.data.local.entity.UserStatus
import com.cyclequest.data.local.preferences.UserPreferences
import com.cyclequest.data.mapper.UserMapper
import com.cyclequest.data.sync.SyncManager
import com.cyclequest.domain.model.User
import com.cyclequest.domain.repository.UserRepository
import com.google.android.gms.common.api.Api
import com.loc.u
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/*
UI设计：注册部件 登陆部件 写一个页面滑动切换
ViewModel实现，livedata转换flow暴露给UI，处理注册用户的逻辑
UserRepository 定义的方法可供viewModel调用来获取domain层数据模型User
repository的flow / userDao的Flow
是否要复用 UserStatus 的状态
 */

    // ViewModel 会协调 UI 层和 Repository 层。它处理用户输入的数据，
    // 调用 UserRepository 来进行用户注册，并通过 LiveData 来通知 UI

// 通过↓注释来标记viewModel
@HiltViewModel
class RegistrationViewModel @Inject constructor (
    private val userRepository: UserRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {
    // 登录状态
    private val _loginState = MutableStateFlow<ApiResult<User>?>(null)
    val loginState: StateFlow<ApiResult<User>?> = _loginState.asStateFlow()

    // 添加当前用户状态
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    init {
        // 初始化时从 SharedPreferences 加载用户
        viewModelScope.launch {
            _currentUser.value = userPreferences.getUser()
        }
    }

    // 在登录成功时保存用户
    private fun saveUser(user: User) {
        userPreferences.saveUser(user)
        _currentUser.value = user
    }

    // 保存用户输入的数据
    val username = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    // 保存注册结果的状态
    /* 为增加null值，更改数据类型为泛类
    _registrationResult 是 MutableLiveData，可以修改值
    registrationResult 是 只读LiveData，只能用于观察，不能从外部(viewModel以外)修改
    安全性：
    防止外部代码直接修改 LiveData 的值
    确保数据修改只能通过 ViewModel 的方法进行 */
    private val _registrationResult = MutableLiveData<ApiResult<User>?>(null)

    val registrationResult: LiveData<ApiResult<User>?> = _registrationResult


    // 本地注册
    // 将本地数据库操作的结果封装成 ApiResult

    //在处理 MutableLiveData 时,我们应该始终使用带下划线的私有变量(registrationResult)来修改值,
    // 而用公开的 registrationResult 来观察值。
    fun userRegisterLocally() {
        // 首先验证输入
        if (!isInputValid()) {
            _registrationResult.value = ApiResult.Error(ApiError.ServerError(getValidationError()))
            return
        }

        val user = User(
            id = "0",
            username = username.value ?: "",
            email = email.value ?: "",
            password = password.value ?: "",
            phoneNumber = "12312341234",
            avatarUrl = "url",
            status = UserStatus.ACTIVE,
            totalRides = 2,
            totalDistance = 1111.11F,
            totalRideTime = 516,
            lastLoginAt = System.currentTimeMillis(),
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
        )

        viewModelScope.launch {
            // 刚进入界面还没有开始时，用户状态不应为正在加载。
            // 默认值应为null
            _registrationResult.value = ApiResult.Loading
            val result = userRepository.UserRegisterLocally(user)
            _registrationResult.value = result
        }
    }

    /* network调用api进行注册的操作
    fun registerUser() {
        val user = User(
            id = 0,  // 初始时 ID 为 0，待数据库自动生成
            username = username.value ?: "",
            email = email.value ?: "",
            password = password.value ?: ""
        )

        viewModelScope.launch {
            _registrationResult.value = ApiResult.Loading  // 显示加载中
            val result = userRepository.registerUser(user)
            _registrationResult.value = result  // 更新注册结果
        }
    }
    */

    // 通过UI进行实时验证--各项输入是否有值及格式
    fun isInputValid(): Boolean {
        return validateUsername(username.value ?: "") == null &&
                validateEmail(email.value ?: "") == null &&
                validatePassword(password.value ?: "") == null
    }

    // 验证用户名不得为空
    fun validateUsername(username: String): String? {
        return when {
            username.isEmpty() -> "用户名不能为空"
            else -> null
        }
    }
    // 验证邮箱不得为空
    fun validateEmail(email: String): String? {
        return when {
            email.isEmpty() -> "邮箱不能为空"
            !isEmailValid(email) -> "请输入有效的邮箱地址"
            else -> null
        }
    }
    // 验证邮箱格式 -- viewmodel内部使用的验证函数
    private fun isEmailValid(email: String): Boolean {
        // 更严格的邮箱验证正则表达式
        val emailPattern = """^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$"""
        return email.matches(emailPattern.toRegex())
    }
    // 验证密码不得为空+长度8位
    fun validatePassword(password: String): String? {
        return when {
            password.isEmpty() -> "密码不能为空"
            //password.length < 8 -> "密码长度至少为8位"
            else -> null
        }
    }

    // 用户邮箱唯一性的验证在 在 UserRepository中进行校验
    // 如果邮箱唯一性错误，会返回：服务器错误


    // 表单提交不再进行格式验证？
    // 建议表单提交时仍然进行一次最终验证
    fun getValidationError(): String {
        return when {
            username.value.isNullOrEmpty() -> "用户名不能为空"
            email.value.isNullOrEmpty() -> "邮箱不能为空"
            !isEmailValid(email.value ?: "") -> "请输入有效的邮箱地址"
            password.value.isNullOrEmpty() -> "密码不能为空"
            else -> ""
        }
    }

    fun loginUser(
        email: String,
        password: String,
        onSuccess: (User) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _loginState.value = ApiResult.Loading
            try {
                val result = userRepository.getUserByEmail(email)
                when {
                    result == null -> {
                        _loginState.value = ApiResult.Error(ApiError.ServerError("邮箱不存在"))
                        onError("邮箱不存在")
                    }
                    result.password != password -> {
                        _loginState.value = ApiResult.Error(ApiError.ServerError("密码错误"))
                        onError("密码错误")
                    }
                    else -> {
                        _loginState.value = ApiResult.Success(result)
                        saveUser(result)    //保存用户信息
                        onSuccess(result)
                    }
                }
            } catch (e: Exception) {
                _loginState.value = ApiResult.Error(ApiError.ServerError(e.message ?: "未知错误"))
                onError(e.message ?: "未知错误")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        // ViewModel 被销毁时清除状态
        _loginState.value = null
    }

    fun skipRegistration() {
        viewModelScope.launch {
            val currentTime = System.currentTimeMillis()
            val defaultUser = User(
                id = "guest_${currentTime}",
                username = "游客用户",
                email = "guest@example.com",
                phoneNumber = null,  // 可选字段，设为 null
                password = "guest_password",
                avatarUrl = null,    // 可选字段，设为 null
                status = UserStatus.ACTIVE,  // 设置用户状态为激活
                totalRides = 0,      // 初始骑行次数为 0
                totalDistance = 0f,  // 初始骑行距离为 0
                totalRideTime = 0L,  // 初始骑行时间为 0
                lastLoginAt = currentTime,  // 设置最后登录时间为当前时间
                createdAt = currentTime,    // 设置创建时间为当前时间
                updatedAt = currentTime     // 设置更新时间为当前时间
            )

            // 1. 先保存到数据库
            val result = userRepository.UserRegisterLocally(defaultUser)

            // 2. 根据保存结果更新状态
            when (result) {
                is ApiResult.Success -> {
                    // 更新注册状态
                    _registrationResult.value = result
                    // 更新登录状态
                    _loginState.value = result
                }
                is ApiResult.Error -> {
                    _registrationResult.value = result
                    _loginState.value = result
                }
                else -> {
                    // 处理其他情况
                }
            }

            /*// 用户追踪用户的登录状态,使用StateFlow进行状态管理，便于LoginScreen观察登录是否成功
            _loginState.value = ApiResult.Success(defaultUser)
            // 将默认用户信息保存到本地
            userRepository.UserRegisterLocally(defaultUser)
            // 更新注册结果状态，便于RegistrationScreen观察注册是否成功
            _registrationResult.value = ApiResult.Success(defaultUser)*/
        }
    }

    // 添加登出方法
    fun logout() {
        viewModelScope.launch {
            userPreferences.clearUser()  // 清除保存的用户信息
            _currentUser.value = null    // 清除当前用户状态
            _loginState.value = null     // 重置登录状态
        }
    }

    // 为ProfileScreen获取用户信息提供函数，暂以userID为例
    fun getCurrentUserId(): String? {
        return currentUser.value?.id
    }


}


//ViewModel 的注入
//通过依赖注入框架（如 Hilt 或 Dagger）来为 RegistrationViewModel 提供依赖
@Module
@InstallIn(ViewModelComponent::class)
object RegistrationModule {

    @Provides
    fun provideUserRepository(
        userDao: UserDao,
        userMapper: UserMapper      // 添加 userMapper 参数
        // syncManager: SyncManager,    // 添加 syncManager 参数
        // databaseSync: DatabaseSync   // 添加 databaseSync 参数
    ): UserRepository {
        return UserRepository(
            userDao,
            userMapper
            // syncManager,
            // databaseSync,
        )
    }




}
