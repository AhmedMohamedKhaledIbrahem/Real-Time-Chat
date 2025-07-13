package com.example.realtimechatapp.features.authentication.domain.usecase.signin

import android.util.Patterns
import com.example.realtimechatapp.core.error.AuthDomainError
import com.example.realtimechatapp.core.utils.Result
import com.example.realtimechatapp.features.authentication.domain.repository.AuthenticationRepository
import com.example.realtimechatapp.features.authentication.domain.validator.EmailValidator

interface SignInUseCase {
    suspend operator fun invoke(email: String, password: String): Result<Unit, AuthDomainError>
}

class SignInUseCaseImpl(
    private val repository: AuthenticationRepository,
    private val emailValidator: EmailValidator,
) : SignInUseCase {
    override suspend fun invoke(email: String, password: String): Result<Unit, AuthDomainError> {
        if (email.isBlank() || password.isBlank()) {
            return Result.Error(AuthDomainError.Network.INVALID_LOGIN_PARAMS)
        }
        if (!emailValidator.isValid(email)) {
            return Result.Error(AuthDomainError.Network.INVALID_EMAIL)
        }
        return repository.signIn(email, password)
    }

}