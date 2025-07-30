package com.example.realtimechat.features.authentication.domain.usecase.forgetpassword

import com.example.realtimechat.core.error.DomainError
import com.example.realtimechat.core.utils.Result
import com.example.realtimechat.features.authentication.domain.repository.AuthenticationRepository

interface ForgetPasswordUseCase {
    suspend operator fun invoke(email: String):  Result<Unit, DomainError>
}
class ForgetPasswordUseCaseImpl(private val repository: AuthenticationRepository) : ForgetPasswordUseCase {
    override suspend fun invoke(email: String): Result<Unit, DomainError> {
        if (email.isBlank() || !email.contains("@",ignoreCase = true,)) {
            return Result.Error(DomainError.Network.INVALID_EMAIL)
        }
      return  repository.forgotPassword(email)
    }
}