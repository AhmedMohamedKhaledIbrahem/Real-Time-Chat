package com.example.realtimechatapp.features.authentication.data.resource.local

import com.example.realtimechatapp.core.database.data.dao.user.UserDao
import com.example.realtimechatapp.core.database.data.entity.user.UserEntity
import com.example.realtimechatapp.core.error.AuthDataError
import com.example.realtimechatapp.core.utils.Result
import com.example.realtimechatapp.features.authentication.data.mapper.toLocalDataError
import com.example.realtimechatapp.features.authentication.data.mapper.toUserEntity
import com.example.realtimechatapp.features.authentication.data.model.SignUpModel
import kotlinx.coroutines.flow.Flow

interface AuthenticationLocalDataSource {
    suspend fun saveUser(
        userParams: SignUpModel,
        uid: String
    ): Result<Unit, AuthDataError.Local>

    suspend fun getUser(): Result<Flow<UserEntity>, AuthDataError.Local>
    suspend fun deleteUser(): Result<Unit, AuthDataError.Local>
    suspend fun updateUser(userParams: UserEntity): Result<Unit, AuthDataError.Local>

    suspend fun activeUserByEmail(email: String): Result<Unit, AuthDataError.Local>
}

class AuthenticationLocalDataSourceImpl(private val dao: UserDao) : AuthenticationLocalDataSource {
    override suspend fun saveUser(
        userParams: SignUpModel,
        uid: String
    ): Result<Unit, AuthDataError.Local> {
        return try {
            val userEntity = userParams.toUserEntity(uid)
            dao.insertUser(userEntity)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.toLocalDataError())
        }
    }

    override suspend fun getUser(): Result<Flow<UserEntity>, AuthDataError.Local> {
       return  try {
           val user = dao.getUser()
           Result.Success(user)
       }catch (e: Exception){
           Result.Error(e.toLocalDataError())
       }
    }

    override suspend fun isUserExist(): Result<UserEntity?, AuthDataError.Local> {
        return try {
            val isUserExist = dao.isUserExist()
            Result.Success(isUserExist)
        } catch (e: Exception) {
            Result.Error(e.toLocalDataError())
        }
    }

    override suspend fun deleteUser(): Result<Unit, AuthDataError.Local> {
       return try {
           dao.deleteUser()
           Result.Success(Unit)
       }catch (e: Exception){
           Result.Error(e.toLocalDataError())
       }
    }

    override suspend fun updateUser(userParams: UserEntity): Result<Unit, AuthDataError.Local> {
        return try {
            dao.updateUser(userParams)
            Result.Success(Unit)
        }catch (e: Exception){
            Result.Error(e.toLocalDataError())
        }
    }

    override suspend fun activeUserByEmail(email: String): Result<Unit, AuthDataError.Local> {
        return try {
            dao.updateIsVerifiedByEmail(email)
            Result.Success(Unit)
        }catch (e: Exception){
            Result.Error(e.toLocalDataError())
        }
    }
}