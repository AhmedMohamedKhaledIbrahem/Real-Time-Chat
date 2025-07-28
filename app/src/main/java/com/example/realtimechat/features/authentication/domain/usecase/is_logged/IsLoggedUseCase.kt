package com.example.realtimechat.features.authentication.domain.usecase.is_logged

import com.example.realtimechat.core.error.AuthDomainError
import com.example.realtimechat.core.utils.Result
import com.example.realtimechat.features.authentication.domain.repository.AuthenticationRepository

interface IsLoggedUseCase {
    suspend operator fun invoke(): Result<Boolean, AuthDomainError>
}

class IsLoggedUseCaseImpl(private val repository: AuthenticationRepository) : IsLoggedUseCase {
    override suspend fun invoke(): Result<Boolean, AuthDomainError> {
        return repository.isLoggedIn()
    }

}