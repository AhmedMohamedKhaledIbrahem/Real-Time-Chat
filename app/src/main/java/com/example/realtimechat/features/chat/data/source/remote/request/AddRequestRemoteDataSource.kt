package com.example.realtimechat.features.chat.data.source.remote.request

import com.example.realtimechat.BuildConfig
import com.example.realtimechat.core.error.DataError
import com.example.realtimechat.core.extension.fold
import com.example.realtimechat.core.firebase.FirebaseInstance
import com.example.realtimechat.core.logger.Logger
import com.example.realtimechat.core.utils.Result
import com.example.realtimechat.core.utils.toRemoteDataError
import com.example.realtimechat.features.chat.data.model.AddRequestModel
import com.example.realtimechat.features.chat.data.model.AddRequestStatus
import com.example.realtimechat.features.chat.data.model.SendFCMRequestModel
import com.example.realtimechat.features.chat.data.model.SendFCMRequestResponse
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

typealias UID = String
typealias FcmToken = String
typealias ChannelUId = String

interface AddRequestRemoteDataSource {

    suspend fun addRequest(
        request: AddRequestModel,
        channelUId: ChannelUId
    ): Result<Unit, DataError>

    suspend fun sendFcmRequest(sendFCMRequestModel: SendFCMRequestModel): Result<Unit, DataError>
    suspend fun getFcmTokenByEmail(receiverEmail: String): Result<FcmToken, DataError>
    suspend fun receiveUid(receiverEmail: String): Result<UID, DataError>
    suspend fun senderUid(): Result<UID, DataError>
    suspend fun addRequestStatus(
        receiverEmail: String,
        status: AddRequestStatus,
        channelUId: ChannelUId
    ): Result<Unit, DataError>

}

class AddRequestRemoteDataSourceImpl(
    private val firebaseInstance: FirebaseInstance,
    private val httpClient: HttpClient,
    logger: Logger
) : AddRequestRemoteDataSource, Logger by logger {


    override suspend fun addRequest(
        request: AddRequestModel,
        channelUId: ChannelUId
    ): Result<Unit, DataError> {
        val senderUser = firebaseInstance.firebaseAuth().currentUser ?: run {
            e(
                FirebaseAuthException(
                    NO_USER_ERROR_CODE,
                    NO_USER_LOGGED_IN_MESSAGE
                ),
                NO_USER_LOGGED_IN_MESSAGE
            )
            throw FirebaseAuthException(
                NO_USER_ERROR_CODE,
                NO_USER_LOGGED_IN_MESSAGE
            )
        }
        val receiverEmail = request.receiverEmail
        val receiverUid = receiveUid(receiverEmail).fold(
            onError = { error ->
                return Result.Error(error)
            },
            onSuccess = { it }
        )
        val senderUid = senderUser.uid
        val valueMapOf: Map<String, Any> = mapOf(
            "senderUid" to senderUid,
            "senderEmail" to request.senderEmail,
            "receiverUid" to receiverUid,
            "receiverEmail" to receiverEmail,
            "timeStamp" to request.timestamp,
            STATUS to AddRequestStatus.PENDING
        )

        val result: Result<Unit, DataError> = try {
            suspendCancellableCoroutine { continuation ->

                firebaseInstance.firebaseDatabase()
                    .getReference(BuildConfig.DB_REFERENCE_CHAT_REQUEST)
                    .child(channelUId)
                    .setValue(valueMapOf)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            continuation.resume(Result.Success(Unit))
                        } else {
                            continuation.resume(
                                Result.Error(
                                    task.exception?.toRemoteDataError() ?: DataError.Network.UNKNOWN
                                )
                            )
                        }
                    }


            }
        } catch (e: Exception) {
            e(throwable = e, message = ERROR_ADD_REQUEST)
            Result.Error(e.toRemoteDataError())
        }
        return result
    }

    override suspend fun sendFcmRequest(sendFCMRequestModel: SendFCMRequestModel): Result<Unit, DataError> {
        val baseUrl = BuildConfig.BASE_URL.plus(BuildConfig.SEND_NOTIFICATION)
        val response = try {
            httpClient.post(
                urlString = baseUrl
            ) {
                contentType(ContentType.Application.Json)
                setBody(sendFCMRequestModel)
            }

        } catch (e: Exception) {
            e(throwable = e, message = ERROR_SEND_FCM_REQUEST)
            return Result.Error(e.toRemoteDataError())
        }
        return when (response.status.value) {
            in 200..299 -> {
                val response = response.body<SendFCMRequestResponse>()
                if (BuildConfig.DEBUG) {
                    d("$RESPONSE${response.success},$MESSAGE_ID${response.messageId}")
                }
                Result.Success(Unit)
            }

            408 -> Result.Error(DataError.Network.TIMEOUT)
            else -> Result.Error(DataError.Network.UNKNOWN)
        }
    }


    override suspend fun addRequestStatus(
        receiverEmail: String,
        status: AddRequestStatus,
        channelUId: ChannelUId
    ): Result<Unit, DataError> {
        val result: Result<Unit, DataError> = try {
            suspendCancellableCoroutine { continuation ->
                firebaseInstance.firebaseDatabase()
                    .getReference(BuildConfig.DB_REFERENCE_CHAT_REQUEST)
                    .child(channelUId)
                    .updateChildren(
                        mapOf(
                            STATUS to status
                        )
                    )
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            continuation.resume(Result.Success(Unit))
                        } else {
                            continuation.resume(
                                Result.Error(
                                    task.exception?.toRemoteDataError() ?: DataError.Network.UNKNOWN
                                )
                            )
                        }
                    }
            }

        } catch (e: Exception) {
            Result.Error(e.toRemoteDataError())
        }
        return result


    }

    override suspend fun senderUid(): Result<UID, DataError> {
        return try {
            val user = firebaseInstance.firebaseAuth().currentUser ?: run {
                e(
                    FirebaseAuthException(
                        NO_USER_ERROR_CODE,
                        NO_USER_LOGGED_IN_MESSAGE
                    ),
                    NO_USER_LOGGED_IN_MESSAGE
                )
                throw FirebaseAuthException(
                    NO_USER_ERROR_CODE,
                    NO_USER_LOGGED_IN_MESSAGE
                )
            }
            Result.Success(user.uid)
        } catch (e: Exception) {
            Result.Error(e.toRemoteDataError())
        }
    }


    override suspend fun receiveUid(receiverEmail: String): Result<UID, DataError> =
        suspendCancellableCoroutine { continuation ->
            val query =
                firebaseInstance.firebaseDatabase().getReference(BuildConfig.DB_REFERENCE)
                    .orderByChild(BuildConfig.Email)
                    .equalTo(receiverEmail)

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val uid = snapshot.children.firstOrNull()?.key
                    if (uid == null) {
                        e(
                            Exception(NO_USER_LOGGED_IN_MESSAGE),
                            NO_USER_LOGGED_IN_MESSAGE
                        )
                        if (continuation.isActive) {
                            continuation.resume(Result.Error(DataError.Network.USER_NOT_FOUND))
                        }
                        return
                    }
                    continuation.resume(Result.Success(uid))
                }

                override fun onCancelled(error: DatabaseError) {
                    val exception = error.toException()
                    e(exception, NO_UID_FOUND)
                    if (continuation.isActive) {
                        continuation.resume(Result.Error(exception.toRemoteDataError()))

                    }
                }
            })

        }

    override suspend fun getFcmTokenByEmail(receiverEmail: String): Result<FcmToken, DataError> =
        suspendCancellableCoroutine { continuation ->
            val query =
                firebaseInstance.firebaseDatabase().getReference(BuildConfig.DB_REFERENCE)
                    .orderByChild(BuildConfig.Email)
                    .equalTo(receiverEmail)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.children.firstOrNull()
                    if (data != null) {
                        val fcmToken = data.child("Fcm").value as? String
                        if (!fcmToken.isNullOrEmpty()) {
                            continuation.resume(Result.Success(fcmToken))
                        } else {
                            continuation.resume(Result.Error(DataError.Network.UNKNOWN))
                        }
                    } else {
                        continuation.resume(Result.Error(DataError.Network.USER_NOT_FOUND))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    val exception = error.toException()
                    e(exception, ERROR_GET_FCM_TOKEN)
                    if (continuation.isActive) {
                        continuation.resume(Result.Error(exception.toRemoteDataError()))
                    }
                }

            })
        }

    companion object {
        private const val NO_UID_FOUND = "no receiver uid found"
        private const val NO_USER_LOGGED_IN_MESSAGE = "no user logged in"
        private const val NO_USER_ERROR_CODE = "NO_USER"
        private const val ERROR_SEND_FCM_REQUEST = "error sending fcm request"
        private const val ERROR_GET_FCM_TOKEN = "Error getting FCM token"
        private const val ERROR_ADD_REQUEST = "Error adding request"
        private const val MESSAGE_ID = "Message ID:"
        private const val RESPONSE = "Response:"
        private const val STATUS = "status"
    }

}