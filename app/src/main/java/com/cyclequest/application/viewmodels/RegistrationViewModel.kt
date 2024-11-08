package com.cyclequest.application.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyclequest.core.database.sync.DatabaseSync
import com.cyclequest.core.network.ApiError
import com.cyclequest.core.network.ApiResult
import com.cyclequest.data.local.dao.UserDao
import com.cyclequest.data.local.entity.UserStatus
import com.cyclequest.data.mapper.UserMapper
import com.cyclequest.data.sync.SyncManager
import com.cyclequest.domain.model.User
import com.cyclequest.domain.repository.UserRepository
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
    private val userRepository: UserRepository
) : ViewModel() {

    // 保存用户输入的数据
    val username = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    // 保存注册结果的状态
    val registrationResult = MutableLiveData<ApiResult<User>>()
    // 本地注册
    //将本地数据库操作的结果封装成 ApiResult
    fun userRegisterLocally() {
        // 首先验证输入
        if (!isInputValid()) {
            registrationResult.value = ApiResult.Error(ApiError.ServerError("请输入所有必填字段"))
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
            registrationResult.value = ApiResult.Loading
            val result = userRepository.UserRegisterLocally(user)
            registrationResult.value = result
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
            registrationResult.value = ApiResult.Loading  // 显示加载中
            val result = userRepository.registerUser(user)
            registrationResult.value = result  // 更新注册结果
        }
    }
    */

    // 校验输入是否合法
    fun isInputValid(): Boolean {
        return !username.value.isNullOrEmpty() &&
                !email.value.isNullOrEmpty() &&
                !password.value.isNullOrEmpty()
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
