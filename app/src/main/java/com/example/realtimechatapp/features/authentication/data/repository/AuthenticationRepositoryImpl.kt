package com.example.realtimechatapp.features.authentication.data.repository

import com.example.internet_connection_monitor.network.InternetConnectionMonitor
import com.example.realtimechatapp.core.error.AuthDataError
import com.example.realtimechatapp.core.error.AuthDomainError
import com.example.realtimechatapp.core.utils.Result
import com.example.realtimechatapp.features.authentication.data.mapper.toDomainError
import com.example.realtimechatapp.features.authentication.data.mapper.toModel
import com.example.realtimechatapp.features.authentication.data.resource.local.AuthenticationLocalDataSource
import com.example.realtimechatapp.features.authentication.data.resource.remote.AuthenticationRemoteDataSource
import com.example.realtimechatapp.features.authentication.domain.entity.SignUpEntity
import com.example.realtimechatapp.features.authentication.domain.repository.AuthenticationRepository

class AuthenticationRepositoryImpl(
    private val localDataSource: AuthenticationLocalDataSource,
    private val remoteDataSource: AuthenticationRemoteDataSource,
    private val internetConnectionMonitor: InternetConnectionMonitor
) : AuthenticationRepository {
    override suspend fun signIn(
        email: String,
        password: String
    ): Result<Unit, AuthDomainError> {
        if (!internetConnectionMonitor.hasConnection()) {
            return Result.Error(AuthDomainError.Network.NETWORK_UNAVAILABLE)
        }
        return when (val result = remoteDataSource.signIn(email, password)) {
            is Result.Success -> {
                val isVerified = result.data
                if (!isVerified) {
                    remoteDataSource.sendEmailVerification()
                    Result.Error(AuthDomainError.Network.EMAIL_NOT_VERIFIED)
                } else {
                    when (val localResult = localDataSource.activeUserByEmail(email)) {
                        is Result.Success -> {
                            Result.Success(Unit)
                        }

                        is Result.Error -> {
                            Result.Error(localResult.error.toDomainError())
                        }
                    }
                }
            }

            is Result.Error -> {

                Result.Error(result.error.toDomainError())
            }
        }
    }

    override suspend fun signUp(signUpParams: SignUpEntity): Result<Unit, AuthDomainError> {
        if (!internetConnectionMonitor.hasConnection()) {
            return Result.Error(AuthDomainError.Network.NETWORK_UNAVAILABLE)
        }
        val signUpModel  = signUpParams.toModel()
        return when (val result = remoteDataSource.signUp(signUpModel)) {
            is Result.Success -> {
                val uid = result.data
                when (val localSaveResult = localDataSource.saveUser(signUpModel, uid)) {
                    is Result.Success -> Result.Success(Unit)
                    is Result.Error -> Result.Error(localSaveResult.error.toDomainError())
                }
            }

            is Result.Error -> {
                Result.Error(result.error.toDomainError())
            }
        }
    }

    override suspend fun forgotPassword(email: String): Result<Unit, AuthDomainError> {
        if (!internetConnectionMonitor.hasConnection()) {
            return Result.Error(AuthDomainError.Network.NETWORK_UNAVAILABLE)
        }
        return when (val result = remoteDataSource.forgotPassword(email)) {
            is Result.Success -> {
                Result.Success(Unit)
            }

            is Result.Error -> {
                Result.Error(result.error.toDomainError())
            }
        }
    }
}