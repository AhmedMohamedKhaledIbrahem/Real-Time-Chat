package com.example.realtimechat.features.chat.domain.usecase.add_request

import com.example.realtimechat.core.error.DomainError
import com.example.realtimechat.core.utils.Result
import com.example.realtimechat.features.chat.data.model.AddRequestModel
import com.example.realtimechat.features.chat.domain.repository.request.AddRequestRepository

interface AddRequestUseCase {
    suspend operator fun invoke(request: AddRequestModel): Result<Unit, DomainError>
}
class AddRequestUseCaseImpl(private val repository: AddRequestRepository) : AddRequestUseCase {
    override suspend fun invoke(request: AddRequestModel): Result<Unit, DomainError> {
        return repository.addRequest(request)
    }

}