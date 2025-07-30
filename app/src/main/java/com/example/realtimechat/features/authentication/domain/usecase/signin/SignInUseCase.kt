package com.example.realtimechat.features.authentication.domain.usecase.signin

import com.example.realtimechat.core.error.DomainError
import com.example.realtimechat.core.utils.Result
import com.example.realtimechat.features.authentication.domain.repository.AuthenticationRepository
import com.example.realtimechat.features.authentication.domain.validator.Validator

interface SignInUseCase {
    suspend operator fun invoke(email: String, password: String): Result<Unit, DomainError>
}

class SignInUseCaseImpl(
    private val repository: AuthenticationRepository,
    private val validator: Validator,
) : SignInUseCase {
    override suspend fun invoke(email: String, password: String): Result<Unit, DomainError> {
        if (email.isBlank() || password.isBlank()) {
            return Result.Error(DomainError.Network.INVALID_LOGIN_PARAMS)
        }
        return repository.signIn(email, password)
    }

}