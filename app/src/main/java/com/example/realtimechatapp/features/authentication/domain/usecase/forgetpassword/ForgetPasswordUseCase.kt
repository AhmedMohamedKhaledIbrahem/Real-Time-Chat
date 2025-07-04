package com.example.realtimechatapp.features.authentication.domain.usecase.forgetpassword

import com.example.realtimechatapp.features.authentication.domain.repository.AuthenticationRepository

interface ForgetPasswordUseCase {
    suspend operator fun invoke(email: String)
}
class ForgetPasswordUseCaseImpl(private val repository: AuthenticationRepository) : ForgetPasswordUseCase {
    override suspend fun invoke(email: String) {
        repository.forgotPassword(email)

    }
}