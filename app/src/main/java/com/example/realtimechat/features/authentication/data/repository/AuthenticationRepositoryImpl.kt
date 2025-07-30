package com.example.realtimechat.features.authentication.data.repository

import com.example.internet_connection_monitor.network.InternetConnectionMonitor
import com.example.realtimechat.core.error.DomainError
import com.example.realtimechat.core.extension.fold
import com.example.realtimechat.core.utils.Result
import com.example.realtimechat.features.authentication.data.mapper.toDomainError
import com.example.realtimechat.features.authentication.data.mapper.toModel
import com.example.realtimechat.features.authentication.data.mapper.toSignUpModel
import com.example.realtimechat.features.authentication.data.resource.local.AuthenticationLocalDataSource
import com.example.realtimechat.features.authentication.data.resource.remote.AuthenticationRemoteDataSource
import com.example.realtimechat.features.authentication.domain.entity.SignUpEntity
import com.example.realtimechat.features.authentication.domain.repository.AuthenticationRepository

class AuthenticationRepositoryImpl(
    private val localDataSource: AuthenticationLocalDataSource,
    private val remoteDataSource: AuthenticationRemoteDataSource,
    private val internetConnectionMonitor: InternetConnectionMonitor
) : AuthenticationRepository {
    override suspend fun signIn(
        email: String,
        password: String
    ): Result<Unit, DomainError> {
        if (!internetConnectionMonitor.hasConnection()) {
            return Result.Error(DomainError.Network.NETWORK_UNAVAILABLE)
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

    override suspend fun sendVerificationEmail(isVerified: Boolean): Result<Unit, DomainError> {
        return if (!isVerified) {
            when (val verificationResult =
                remoteDataSource.sendEmailVerification()) {
                is Result.Success -> Result.Error(DomainError.Network.EMAIL_NOT_VERIFIED)
                is Result.Error -> Result.Error(verificationResult.error.toDomainError())
            }
        } else {
            Result.Success(Unit)
        }

    }

    override suspend fun saveUser(isVerified: Boolean): Result<Unit, DomainError> {
        return localDataSource.isUserExist().fold(
            onError = {
                Result.Error(it.toDomainError())
            },
            onSuccess = { userEntity ->
                when {
                    !internetConnectionMonitor.hasConnection() -> {
                        return Result.Error(DomainError.Network.NETWORK_UNAVAILABLE)
                    }
                    userEntity == null && isVerified -> {
                        return remoteDataSource.fetchUser().fold(
                            onError = {
                                Result.Error(it.toDomainError())
                            },
                            onSuccess = {
                                val (remoteUser, token) = it
                                localDataSource.saveUser(
                                    remoteUser.toSignUpModel(),
                                    token
                                ).fold(
                                    onError = { error ->
                                        Result.Error(error.toDomainError())
                                    },
                                    onSuccess = {
                                        Result.Success(Unit)
                                    }
                                )
                            },
                        )

                    }
                }
                return Result.Success(Unit)
            }
        )
    }

    override suspend fun activeUserByEmail(email: String): Result<Unit, DomainError> {
        return when (val localResult = localDataSource.activeUserByEmail(email)) {
            is Result.Success -> Result.Success(Unit)
            is Result.Error -> Result.Error(localResult.error.toDomainError())
        }
    }


    override suspend fun signUp(signUpParams: SignUpEntity): Result<Unit, DomainError> {
        if (!internetConnectionMonitor.hasConnection()) {
            return Result.Error(DomainError.Network.NETWORK_UNAVAILABLE)
        }
        val signUpModel = signUpParams.toModel()

        return remoteDataSource.signUp(signUpModel).fold(
            onError = {
                Result.Error(it.toDomainError())
            },
            onSuccess = { signUpResult ->
                val uid = signUpResult
                return localDataSource.saveUser(signUpModel, uid).fold(
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

    override suspend fun forgotPassword(email: String): Result<Unit, DomainError> {
        if (!internetConnectionMonitor.hasConnection()) {
            return Result.Error(DomainError.Network.NETWORK_UNAVAILABLE)
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

    override suspend fun isLoggedIn(): Result<Boolean, DomainError> {
        if (!internetConnectionMonitor.hasConnection()) {
            return Result.Error(DomainError.Network.NETWORK_UNAVAILABLE)
        }
        return remoteDataSource.isLoggedIn().fold(
            onError = {
                Result.Error(it.toDomainError())
            },
            onSuccess = {
                Result.Success(it)
            },
        )
    }
}