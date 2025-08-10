package com.example.realtimechat.features.chat.data.source.local

import com.example.realtimechat.core.database.data.dao.chat_request.ChatRequestDao
import com.example.realtimechat.core.database.data.dao.user.UserDao
import com.example.realtimechat.core.database.data.entity.chat_request.ChatRequestEntity
import com.example.realtimechat.core.error.DataError
import com.example.realtimechat.core.logger.Logger
import com.example.realtimechat.core.utils.Result
import com.example.realtimechat.core.utils.toLocalDataError

typealias SenderEmail = String
typealias UidSender = String

interface AddRequestLocalDataSource {
    suspend fun saveChatRequest(chatRequest: ChatRequestEntity): Result<Unit, DataError>
    suspend fun getChatRequest(): Result<List<ChatRequestEntity?>, DataError>
    suspend fun getSenderUser(): Result<Pair<UidSender, SenderEmail>, DataError>
    suspend fun deleteChatRequest(): Result<Unit, DataError>
}

class AddRequestLocalDataSourceImpl(
    private val chatRequestDao: ChatRequestDao,
    private val userDao: UserDao,
    logger: Logger
) : AddRequestLocalDataSource, Logger by logger {
    override suspend fun saveChatRequest(chatRequest: ChatRequestEntity): Result<Unit, DataError> {
        return try {
            chatRequestDao.insertChatRequest(chatRequest)
            Result.Success(Unit)
        } catch (e: Exception) {
            e(e, ERROR_SAVE_CHAT_REQUEST)
            Result.Error(e.toLocalDataError())
        }
    }

    override suspend fun getChatRequest(): Result<List<ChatRequestEntity?>, DataError> {
        return try {
            val chatRequests = chatRequestDao.getChatRequests()
            Result.Success(chatRequests)

        } catch (e: Exception) {
            e(e, ERROR_GET_CHAT_REQUEST)
            Result.Error(e.toLocalDataError())
        }
    }

    override suspend fun getSenderUser(): Result<Pair<UidSender, SenderEmail>, DataError> {
        return try {
            val user = userDao.getUser()
            Result.Success(Pair(user.id, user.email))
        } catch (e: Exception) {
            e(e, ERROR_GET_CHAT_REQUEST)
            Result.Error(e.toLocalDataError())
        }
    }

    override suspend fun deleteChatRequest(): Result<Unit, DataError> {
        return try {
            chatRequestDao.deleteChatRequest()
            Result.Success(Unit)
        } catch (e: Exception) {
            e(e, ERROR_DELETE_CHAT_REQUEST)
            Result.Error(e.toLocalDataError())
        }
    }

    companion object {
        private const val ERROR_SAVE_CHAT_REQUEST = "Failed to save chat request"
        private const val ERROR_GET_CHAT_REQUEST = "Failed to get chat request"
        private const val ERROR_DELETE_CHAT_REQUEST = "Failed to delete chat request"
    }
}