package com.example.realtimechatapp.features.authentication.domain.usecase.forgetpassword

import com.example.realtimechatapp.core.error.AuthDomainError
import com.example.realtimechatapp.core.utils.Result
import com.example.realtimechatapp.features.authentication.domain.repository.AuthenticationRepository

interface ForgetPasswordUseCase {
    suspend operator fun invoke(email: String):  Result<Unit, AuthDomainError>
}
class ForgetPasswordUseCaseImpl(private val repository: AuthenticationRepository) : ForgetPasswordUseCase {
    override suspend fun invoke(email: String): Result<Unit, AuthDomainError> {
        if (email.isBlank() || !email.contains("@",ignoreCase = true,)) {
            return Result.Error(AuthDomainError.Network.INVALID_EMAIL)
        }
      return  repository.forgotPassword(email)
    }
}