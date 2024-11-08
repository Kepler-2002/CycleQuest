package com.cyclequest.data.local.dao

import androidx.room.*
import com.cyclequest.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Transaction
    suspend fun upsertAll(entities: List<UserEntity>) {
        deleteAll()
        insertAll(entities)
    }

    @Query("DELETE FROM users")
    suspend fun deleteAll()
    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUser(userId: String)



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<UserEntity>)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)
    // 查询 email 限制 1
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?


    @Query("SELECT * FROM users")
    fun getAllUsersFlow(): Flow<List<UserEntity>>
    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<UserEntity>
    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: String): UserEntity?


    @Update
    suspend fun updateUser(user: UserEntity)

}
