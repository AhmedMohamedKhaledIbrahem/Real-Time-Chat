package com.example.realtimechatapp.features.authentication.data.repository

import com.example.internet_connection_monitor.network.InternetConnectionMonitor
import com.example.realtimechatapp.core.error.AuthDomainError
import com.example.realtimechatapp.core.extension.fold
import com.example.realtimechatapp.core.utils.Result
import com.example.realtimechatapp.features.authentication.data.mapper.toDomainError
import com.example.realtimechatapp.features.authentication.data.mapper.toModel
import com.example.realtimechatapp.features.authentication.data.mapper.toSignUpModel
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

        return remoteDataSource.signIn(email, password).fold(
            onError = {
                Result.Error(it.toDomainError())
            },
            onSuccess = { signInResult ->
                val isVerified = signInResult
                val verificationResult = sendVerificationEmail(isVerified)
                if (verificationResult is Result.Error) return@fold verificationResult
                val saveUserResult = saveUser(isVerified)
                if (saveUserResult is Result.Error) return@fold saveUserResult
                return@fold activeUserByEmail(email)
            }
        )
    }

    override suspend fun sendVerificationEmail(isVerified: Boolean): Result<Unit, AuthDomainError> {
        return if (!isVerified) {
            when (val verificationResult =
                remoteDataSource.sendEmailVerification()) {
                is Result.Success -> Result.Error(AuthDomainError.Network.EMAIL_NOT_VERIFIED)
                is Result.Error -> Result.Error(verificationResult.error.toDomainError())
            }
        } else {
            Result.Success(Unit)
        }

    }

    override suspend fun saveUser(isVerified: Boolean): Result<Unit, AuthDomainError> {
        return when (val isUserExist = localDataSource.isUserExist()) {
            is Result.Error -> Result.Error(isUserExist.error.toDomainError())
            is Result.Success -> {
                val user = isUserExist.data
                if (user == null && isVerified) {
                    when (val fetchUserResult = remoteDataSource.fetchUser()) {
                        is Result.Error -> {
                            return Result.Error(fetchUserResult.error.toDomainError())
                        }

                        is Result.Success -> {
                            val (remoteUser, token) = fetchUserResult.data
                            localDataSource.saveUser(
                                remoteUser.toSignUpModel(),
                                token
                            )
                        }
                    }
                }
                Result.Success(Unit)
            }
        }
    }

    override suspend fun activeUserByEmail(email: String): Result<Unit, AuthDomainError> {
        return when (val localResult = localDataSource.activeUserByEmail(email)) {
            is Result.Success -> Result.Success(Unit)
            is Result.Error -> Result.Error(localResult.error.toDomainError())
        }
    }


    override suspend fun signUp(signUpParams: SignUpEntity): Result<Unit, AuthDomainError> {
        if (!internetConnectionMonitor.hasConnection()) {
            return Result.Error(AuthDomainError.Network.NETWORK_UNAVAILABLE)
        }
        val signUpModel = signUpParams.toModel()

        return remoteDataSource.signUp(signUpModel).fold(
            onError = {
                Result.Error(it.toDomainError())
            },
            onSuccess = { signUpResult ->
                val uid = signUpResult
                return@fold localDataSource.saveUser(signUpModel, uid).fold(
                    onError = {
                        Result.Error(it.toDomainError())
                    },
                    onSuccess = {
                        Result.Success(Unit)
                    },
                )
            }
        )
    }

    override suspend fun forgotPassword(email: String): Result<Unit, AuthDomainError> {
        if (!internetConnectionMonitor.hasConnection()) {
            return Result.Error(AuthDomainError.Network.NETWORK_UNAVAILABLE)
        }
        return remoteDataSource.forgotPassword(email).fold(
            onError = {
                Result.Error(it.toDomainError())
            },
            onSuccess = {
                Result.Success(it)
            },
        )
    }
}