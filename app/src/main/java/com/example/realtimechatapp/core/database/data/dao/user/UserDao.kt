package com.example.realtimechatapp.core.database.data.dao.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.realtimechatapp.core.database.data.entity.user.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userParams: UserEntity)

    @Query("SELECT * FROM user")
    suspend fun getUser(): Flow<UserEntity>

    @Update
    suspend fun updateUser(user: UserEntity)
    
    @Query("UPDATE user SET isVerified = 1 WHERE email = :email")
    suspend fun updateIsVerifiedByEmail(email: String)
    @Query("DELETE FROM user")
    suspend fun deleteUser()
}