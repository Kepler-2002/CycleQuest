# 关键设计理念

## 1. 本地数据库核心架构

- 实现了基于 Room 的本地数据库架构
- 引入了 Repository 模式统一数据访问层
- 使用 Hilt 进行依赖注入，实现了单例模式的数据库实例

### Repository 模式
- Repository负责协调本地与远程数据
#### 如何使用?
- UI层(ViewModel)通过Repository订阅数据变化
- 数据修改统一经过Repository
- Repository自动处理本地存储和远程同步

### Repository 模式下的数据流转方式
1. UI层通过ViewModel订阅Repository的Flow
2. 数据修改通过Repository进行
3. Repository更新本地数据库
4. 数据库变化通过Flow通知UI更新
5. Repository同时触发后台同步
6. 远程同步完成后更新本地数据库
7. UI自动获得最新数据


## 2. 数据同步机制

### Single Source of Truth 模式
- 以本地数据库作为唯一的数据真相源
- 所有数据操作必须通过本地数据库，包括展示和修改
- 远程数据获取后先存入本地数据库，再由UI通过观察本地数据库获取更新
- 为什么这样做?
  - 确保数据一致性，避免多数据源导致的数据不一致
  - 支持离线操作，提升用户体验
  - 简化数据流向，,降低开发复杂度

### 核心组件

- `SyncAdapter`: 抽象同步适配器接口，支持通用 CRUD 操作
- `SyncType`: 封装同步配置，支持不同实体的自定义同步策略

### 重试机制

- 支持自定义重试次数和间隔时间
- 实现了指数退避和线性重试策略
- 通过 RetryPolicy 抽象类实现策略模式


## 3. 其他设计

### entity设计
- 用BaseEntity 接口，统一实体行为
- 同步状态追踪，支持创建时间和更新时间的自动管理
- 
### DAO 层设计
- Flow 响应式数据流
- 完整的 CRUD 操作

### 依赖注入设计
- 统一在 Module 中提供依赖
- Qualifiers 注解区分不同实例
- Scope 控制实例生命周期
### 错误处理机制
- 统一的错误类型定义
- 错误传播和转换规则
- 错误恢复策略

## 4. 样例代码

### Repository 模式
```kotlin
class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val userApi: UserApi,
    private val userMapper: UserMapper
) {
    // UI层通过Flow观察数据变化
    fun observeUser(userId: String): Flow<User> = 
        userDao.observeById(userId).map { userMapper.toModel(it) }

    // 数据修改统一经过Repository
    suspend fun updateUser(user: User) {
        userDao.update(userMapper.toEntity(user))
        // 触发后台同步
        syncManager.scheduleSyncFor(SyncType.USER)
    }
}
```
### 同步适配器实现
```kotlin
class UserSyncAdapter @Inject constructor(
    private val api: UserApi,
    private val dao: UserDao,
    private val mapper: UserMapper
) : SyncAdapter<User> {
    override suspend fun getAll(): List<User> = 
        api.getUsers().map { mapper.toModel(it) }

    override suspend fun update(entities: List<User>) {
        withContext(Dispatchers.IO) {
            val dtos = entities.map { mapper.toDto(it) }
            val response = api.updateUsers(dtos)
            dao.upsertAll(response.map { mapper.toEntity(it) })
        }
    }
}
```

### ViewModel使用示例
```kotlin
@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val syncManager: SyncManager
) : ViewModel() {
    val users = userRepository.getUsers()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val syncStatus = syncManager.status
        .stateIn(viewModelScope, SharingStarted.Eagerly, SyncStatus.Idle)

    fun refresh() {
        viewModelScope.launch {
            syncManager.sync(SyncType.USER)
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            userRepository.updateUser(user)
        }
    }
}
```

### Domain层与Data层分离
```kotlin
// Domain层定义业务模型
data class User(
    val id: String,
    val name: String,
    val profile: Profile
)

// Data层定义数据实体
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey override val id: String,
    val name: String,
    val profileId: String,
    override val syncStatus: SyncStatus,
    override val createdAt: Long,
    override val updatedAt: Long
) : BaseEntity
```

### DAO实现示例
```kotlin
@Dao
interface UserDao : BaseDao<UserEntity> {
    @Query("SELECT * FROM users")
    fun observeAll(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE id = :userId")
    fun observeById(userId: String): Flow<UserEntity?>

    @Transaction
    suspend fun upsertAll(entities: List<UserEntity>) {
        deleteAll()
        insertAll(entities)
    }
}
```

### 数据库配置与初始化
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabaseConfig() = DatabaseConfig(
        name = "app.db",
        version = BuildConfig.DB_VERSION,
        enableWAL = true,
        enableForeignKeys = true
    )

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        config: DatabaseConfig,
        migrations: DatabaseMigrationProvider
    ): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, config.name)
            .addMigrations(*migrations.getMigrations())
            .build()
    }
}
```




