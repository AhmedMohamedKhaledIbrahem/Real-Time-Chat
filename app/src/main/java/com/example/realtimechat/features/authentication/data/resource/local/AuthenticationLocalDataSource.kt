package com.example.realtimechat.features.authentication.data.resource.local

import com.example.realtimechat.core.database.data.dao.user.UserDao
import com.example.realtimechat.core.database.data.entity.user.UserEntity
import com.example.realtimechat.core.error.DataError
import com.example.realtimechat.core.logger.Logger
import com.example.realtimechat.core.utils.Result
import com.example.realtimechat.features.authentication.data.mapper.toLocalDataError
import com.example.realtimechat.features.authentication.data.mapper.toUserEntity
import com.example.realtimechat.features.authentication.data.model.SignUpModel
import kotlinx.coroutines.flow.Flow

interface AuthenticationLocalDataSource {
    suspend fun saveUser(
        userParams: SignUpModel,
        uid: String
    ): Result<Unit, DataError.Local>

    suspend fun getUser(): Result<Flow<UserEntity>, DataError.Local>
    suspend fun isUserExist(): Result<UserEntity?, DataError.Local>
    suspend fun deleteUser(): Result<Unit, DataError.Local>
    suspend fun updateUser(userParams: UserEntity): Result<Unit, DataError.Local>

    suspend fun activeUserByEmail(email: String): Result<Unit, DataError.Local>
}

class AuthenticationLocalDataSourceImpl(
    private val dao: UserDao,
    logger: Logger
) : AuthenticationLocalDataSource , Logger by logger {
    override suspend fun saveUser(
        userParams: SignUpModel,
        uid: String
    ): Result<Unit, DataError.Local> {
        return try {
            val userEntity = userParams.toUserEntity(uid)
            dao.insertUser(userEntity)
            Result.Success(Unit)
        } catch (e: Exception) {
            e(throwable = e , message = "Error saving user")
            Result.Error(e.toLocalDataError())
        }
    }

    override suspend fun getUser(): Result<Flow<UserEntity>, DataError.Local> {
        return try {
            val user = dao.getUser()
            Result.Success(user)
        } catch (e: Exception) {
            e(throwable = e , message = "Error getting user")
            Result.Error(e.toLocalDataError())
        }
    }

    override suspend fun isUserExist(): Result<UserEntity?, DataError.Local> {
        return try {
            val isUserExist = dao.isUserExist()
            Result.Success(isUserExist)
        } catch (e: Exception) {
            e(throwable = e , message = "Error checking if user exist")
            Result.Error(e.toLocalDataError())
        }
    }

    override suspend fun deleteUser(): Result<Unit, DataError.Local> {
        return try {
            dao.deleteUser()
            Result.Success(Unit)
        } catch (e: Exception) {
            e(throwable = e , message = "Error deleting user")
            Result.Error(e.toLocalDataError())
        }
    }

    override suspend fun updateUser(userParams: UserEntity): Result<Unit, DataError.Local> {
        return try {
            dao.updateUser(userParams)
            Result.Success(Unit)
        } catch (e: Exception) {
            e(throwable = e , message = "Error updating user")
            Result.Error(e.toLocalDataError())
        }
    }

    override suspend fun activeUserByEmail(email: String): Result<Unit, DataError.Local> {
        return try {
            dao.updateIsVerifiedByEmail(email)
            Result.Success(Unit)
        } catch (e: Exception) {
            e(throwable = e , message = "Error activating user by email")
            Result.Error(e.toLocalDataError())
        }
    }
}