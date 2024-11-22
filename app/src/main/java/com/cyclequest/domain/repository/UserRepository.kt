package com.cyclequest.domain.repository


import com.cyclequest.core.database.sync.DatabaseSync
import com.cyclequest.core.database.sync.SyncStatus
import com.cyclequest.core.network.ApiError
import com.cyclequest.core.network.ApiResult
import com.cyclequest.core.network.ApiService
import com.cyclequest.data.local.dao.UserDao
import com.cyclequest.data.local.entity.UserEntity
import com.cyclequest.data.mapper.UserMapper
import com.cyclequest.data.sync.SyncManager
import com.cyclequest.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/* repository 负责封装数据，协调数据访问，repository调用Dao实现从数据库获取用户或插入新用户。
repository处理业务逻辑-用户id默认生成 唯一性检验，密码格式校验，邮箱格式校验
repository方法返回值类型 是userDao定义的getter类方法通过mapper转换至toDomain
还可以是viewModel从UI提交表单的数据，在此处定义方法转换至toLocal/toEntity
*/

@Singleton
class
UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val userMapper: UserMapper,
    // private val syncManager: SyncManager,
    // private val databaseSync: DatabaseSync
) {
    fun getUsers(): Flow<List<User>> {
        return userDao.getAllUsersFlow()
            .map { users -> users.map(userMapper::toDomain) }
    }

    suspend fun getUser(id: String): User? {
        // 移除同步检查
        /*if (databaseSync.shouldSync(SyncType.User)) {
            syncManager.startSync()
        }*/
        return userDao.getUserById(id)?.let { userMapper.toDomain(it) }
    }

    suspend fun updateUser(user: User) {
        /*val localUser = userMapper.toLocal(user).copy(syncStatus = SyncStatus.PENDING)
        userDao.updateUser(localUser)
        syncManager.startSync()*/

        val localUser = userMapper.toLocal(user)
        userDao.updateUser(localUser)
    }

    suspend fun refreshUsers() {
        // syncManager.startSync()
    }



    //在本地通过 ROOM 进行用户注册
    suspend fun UserRegisterLocally(user: User): ApiResult<User> {
        // 1. 从云端 同步User （UserID）
        // 2. 从UserDao里面找有没有已经注册过的UserID

        // @lsylion: UserID 建议系统生成，此处绑定用户唯一性到邮箱
        // 检查邮箱是否存在
        val existingUser = userDao.getUserByEmail(user.email)
        if (existingUser != null) {
            return ApiResult.Error(ApiError.ServerError("User with this email has already existed"))
        }

        // 3. 如果不重复，就写UserDao
        // 生成UUID作为用户ID
        val userWithId = user.copy(id = UUID.randomUUID().toString())
        val userEntity = userMapper.toLocal(userWithId).copy(
            // syncStatus = SyncStatus.PENDING
        )

        try {
            userDao.insertUser(userEntity)
            return ApiResult.Success(userWithId)
        } catch (e: Exception) {
            return ApiResult.Error(ApiError.ServerError("Failed to register user: ${e.message}"))
        }

        /*使用UUID.randomUUID()自动生成唯一ID
        在插入数据库前，先为用户对象设置ID
        设置syncStatus为PENDING，表示需要同步到远程服务器
        使用try-catch处理数据库操作可能的异常
        返回带有新ID的用户对象*/
    }

    // 用户登陆时获取邮箱
    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)?.let { userEntity ->
            userMapper.toDomain(userEntity)
        }

        // 4. 同步云端
        /* 如果通过网络请求,也可以返回类似 ApiResult 类型的结果
    suspend fun UserRegisterNetwork(user: User): ApiResult<User> {
        return try {
            // 调用 API 注册
            val response = ApiService.registerUser(user)
            if (response.isSuccessful) {
                ApiResult.Success(user)
            } else {
                ApiResult.Error(ApiError.HttpError(response.code(), response.message()))
            }
        } catch (e: Exception) {
            ApiResult.Error(ApiError.NetworkError(e))
        }
    }
    */
    }
}
