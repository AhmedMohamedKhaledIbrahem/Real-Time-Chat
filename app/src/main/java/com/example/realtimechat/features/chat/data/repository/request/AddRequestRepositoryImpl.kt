package com.example.realtimechat.features.chat.data.repository.request

import com.example.internet_connection_monitor.network.InternetConnectionMonitor
import com.example.realtimechat.core.database.data.entity.chat_request.ChatRequestEntity
import com.example.realtimechat.core.error.DomainError
import com.example.realtimechat.core.extension.fold
import com.example.realtimechat.core.shared_preference.RealTimeChatSharedPreference
import com.example.realtimechat.core.utils.Result
import com.example.realtimechat.core.utils.toDomainError
import com.example.realtimechat.features.chat.data.model.AddRequestModel
import com.example.realtimechat.features.chat.data.model.AddRequestStatus
import com.example.realtimechat.features.chat.data.model.SendFCMRequestModel
import com.example.realtimechat.features.chat.data.source.local.AddRequestLocalDataSource
import com.example.realtimechat.features.chat.data.source.remote.request.AddRequestRemoteDataSource
import com.example.realtimechat.features.chat.domain.repository.request.AddRequestRepository
import com.example.realtimechat.features.chat.domain.repository.request.ChannelUid
import com.example.realtimechat.features.chat.domain.repository.request.ReceiverEmail
import com.example.realtimechat.features.chat.domain.repository.request.ReceiverUid
import com.example.realtimechat.features.chat.domain.repository.request.SenderEmail
import com.example.realtimechat.features.chat.domain.repository.request.UidSender
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.UUID

class AddRequestRepositoryImpl(
    private val remoteDataSource: AddRequestRemoteDataSource,
    private val localDataSource: AddRequestLocalDataSource,
    private val sharedPreference: RealTimeChatSharedPreference,
    private val internetConnectionMonitor: InternetConnectionMonitor
) : AddRequestRepository {


    override suspend fun sendFcmRequest(sendFcmRequestParams: SendFCMRequestModel): Result<Unit, DomainError> {
        if (!internetConnectionMonitor.hasConnection()) return Result.Error(DomainError.Network.NETWORK_UNAVAILABLE)
        val result = remoteDataSource.sendFcmRequest(sendFcmRequestParams).fold(
            onError = { return Result.Error(it.toDomainError()) },
            onSuccess = { it }
        )
        return Result.Success(result)

    }

    override suspend fun addRequest(request: AddRequestModel): Result<Unit, DomainError> {
        if (!internetConnectionMonitor.hasConnection()) return Result.Error(DomainError.Network.NETWORK_UNAVAILABLE)
        val existingUid = sharedPreference.getChannelUid()


        val channelUId = if (existingUid.isNullOrEmpty()) {
            val newUid = UUID.randomUUID().toString()
            sharedPreference.saveChannelUid(newUid)
            newUid
        } else {
            existingUid
        }

        val result = remoteDataSource.addRequest(request, channelUId).fold(
            onError = { return Result.Error(it.toDomainError()) },
            onSuccess = { it }
        )
        return Result.Success(result)
    }

    override suspend fun addRequestStatus(
        receiverEmail: ReceiverEmail,
        status: AddRequestStatus,
        channelUid: ChannelUid
    ): Result<Unit, DomainError> {
        if (!internetConnectionMonitor.hasConnection()) return Result.Error(DomainError.Network.NETWORK_UNAVAILABLE)

        val result = remoteDataSource.addRequestStatus(
            receiverEmail,
            status,
            channelUid
        ).fold(
            onError = { return Result.Error(it.toDomainError()) },
            onSuccess = { it }
        )
        return Result.Success(result)
    }

    override suspend fun getSenderUser(): Result<Pair<UidSender, SenderEmail>, DomainError> {
        val result = localDataSource.getSenderUser().fold(
            onError = { return Result.Error(it.toDomainError()) },
            onSuccess = { it }
        )
        return Result.Success(result)
    }

    override suspend fun getReceiverUid(receiverEmail: ReceiverEmail): Result<ReceiverUid, DomainError> {
        if (!internetConnectionMonitor.hasConnection()) return Result.Error(DomainError.Network.NETWORK_UNAVAILABLE)
        val result = remoteDataSource.receiveUid(receiverEmail).fold(
            onError = { return Result.Error(it.toDomainError()) },
            onSuccess = { it }
        )
        return Result.Success(result)
    }

    override suspend fun saveChatRequest(chatRequestEntity: ChatRequestEntity): Result<Unit, DomainError> {
        val result = localDataSource.saveChatRequest(chatRequestEntity).fold(
            onError = { return Result.Error(it.toDomainError()) },
            onSuccess = { it }
        )
        return Result.Success(result)
    }


    override suspend fun getFcmTokenByEmail(email: ReceiverEmail): Result<Unit, DomainError> {
        if (!internetConnectionMonitor.hasConnection()) return Result.Error(DomainError.Network.NETWORK_UNAVAILABLE)
        val result = remoteDataSource.getFcmTokenByEmail(email).fold(
            onError = { return Result.Error(it.toDomainError()) },
            onSuccess = { sharedPreference.saveReceiverToken(token = it) },
        )
        return Result.Success(result)
    }
}