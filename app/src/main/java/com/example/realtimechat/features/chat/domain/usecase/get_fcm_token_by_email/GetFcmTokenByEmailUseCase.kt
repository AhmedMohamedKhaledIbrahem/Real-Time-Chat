package com.example.realtimechat.features.chat.domain.usecase.get_fcm_token_by_email

import com.example.realtimechat.core.error.DomainError
import com.example.realtimechat.core.utils.Result
import com.example.realtimechat.features.chat.domain.repository.request.AddRequestRepository

interface GetFcmTokenByEmailUseCase {
    suspend operator fun invoke(email: String): Result<Unit, DomainError>
}

class GetFcmTokenByEmailUseCaseImpl(
    private val repository: AddRequestRepository
) : GetFcmTokenByEmailUseCase {
    override suspend fun invoke(email: String): Result<Unit, DomainError> {
        return repository.getFcmTokenByEmail(email)
    }
}