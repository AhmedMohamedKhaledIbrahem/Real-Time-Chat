package com.example.realtimechat.features.authentication.domain.usecase.signup

import com.example.realtimechat.core.error.AuthDomainError
import com.example.realtimechat.core.utils.Result
import com.example.realtimechat.features.authentication.domain.entity.SignUpEntity
import com.example.realtimechat.features.authentication.domain.repository.AuthenticationRepository

interface SignUpUseCase {
    suspend operator fun invoke(signUpParams: SignUpEntity): Result<Unit, AuthDomainError>
}

class SignUpUseCaseImpl(private val repository: AuthenticationRepository) : SignUpUseCase {
    override suspend fun invoke(signUpParams: SignUpEntity): Result<Unit, AuthDomainError> {
        if (signUpParams.isValid()) {
            return Result.Error(AuthDomainError.Network.INVALID_SIGN_UP_PARAMS)
        }
        return repository.signUp(signUpParams)

    }
}