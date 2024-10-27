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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<UserEntity>)

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<UserEntity>

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("SELECT * FROM users")
    fun getAllUsersFlow(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUser(userId: String)
}
