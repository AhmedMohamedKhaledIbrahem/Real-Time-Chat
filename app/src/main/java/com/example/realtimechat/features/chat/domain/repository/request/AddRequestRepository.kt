package com.example.realtimechat.features.chat.domain.repository.request

import com.example.realtimechat.core.database.data.entity.chat_request.ChatRequestEntity
import com.example.realtimechat.core.error.DomainError
import com.example.realtimechat.core.utils.Result
import com.example.realtimechat.features.chat.data.model.AddRequestModel
import com.example.realtimechat.features.chat.data.model.AddRequestStatus
import com.example.realtimechat.features.chat.data.model.SendFCMRequestModel

typealias ChannelUid = String
typealias ReceiverEmail = String
typealias ReceiverUid = String
typealias SenderEmail = String
typealias UidSender = String

interface AddRequestRepository {
    suspend fun sendFcmRequest(sendFcmRequestParams: SendFCMRequestModel): Result<Unit, DomainError>
    suspend fun addRequest(request: AddRequestModel): Result<Unit, DomainError>
    suspend fun addRequestStatus(
        receiverEmail: ReceiverEmail,
        status: AddRequestStatus,
        channelUid: ChannelUid
    ): Result<Unit, DomainError>

    suspend fun getSenderUser(): Result<Pair<UidSender, SenderEmail>, DomainError>

    suspend fun getReceiverUid(receiverEmail: ReceiverEmail): Result<ReceiverUid, DomainError>
    suspend fun saveChatRequest(chatRequestEntity: ChatRequestEntity): Result<Unit, DomainError>
    suspend fun getFcmTokenByEmail(email: ReceiverEmail): Result<Unit, DomainError>
}