package com.example.realtimechat.features.chat.domain.usecase.send_fcm_request

import com.example.realtimechat.core.error.DomainError
import com.example.realtimechat.core.utils.Result
import com.example.realtimechat.features.chat.data.model.SendFCMRequestModel
import com.example.realtimechat.features.chat.domain.repository.request.AddRequestRepository

interface SendFcmRequestUseCase {
    suspend operator fun invoke(sendFcmRequestParams: SendFCMRequestModel): Result<Unit, DomainError>
}

class SendFcmRequestUseCaseImpl(
    private val repository: AddRequestRepository
) : SendFcmRequestUseCase {
    override suspend fun invoke(sendFcmRequestParams: SendFCMRequestModel): Result<Unit, DomainError> {
        return repository.sendFcmRequest(sendFcmRequestParams)
    }

}