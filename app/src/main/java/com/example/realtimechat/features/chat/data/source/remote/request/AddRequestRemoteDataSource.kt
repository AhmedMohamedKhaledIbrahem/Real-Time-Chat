package com.example.realtimechat.features.chat.data.source.remote.request

import com.example.realtimechat.BuildConfig
import com.example.realtimechat.core.error.DataError
import com.example.realtimechat.core.extension.fold
import com.example.realtimechat.core.firebase.FirebaseInstance
import com.example.realtimechat.core.logger.Logger
import com.example.realtimechat.core.utils.Result
import com.example.realtimechat.features.authentication.data.mapper.toRemoteDataError
import com.example.realtimechat.features.chat.data.model.AddRequestModel
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume

typealias UID = String

interface AddRequestRemoteDataSource {
    suspend fun addRequest(request: AddRequestModel): Result<Unit, DataError>
}

class AddRequestRemoteDataSourceImpl(
    private val firebaseInstance: FirebaseInstance,
    logger: Logger
) : AddRequestRemoteDataSource, Logger by logger {
    override suspend fun addRequest(request: AddRequestModel): Result<Unit, DataError> {
        return try {
            val receiverEmail = request.receiverEmail
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
            val receiverUid = receiveUid(receiverEmail)
            val senderUid = senderUser.uid
            return receiverUid.fold(
                onError = { error ->
                    Result.Error(error)
                },
                onSuccess = { uid ->
                    val firebaseAsync =
                        firebaseInstance.firebaseDatabase()
                            .getReference(BuildConfig.DB_REFERENCE_CHAT_REQUEST)
                            .child(uid).child(senderUid).setValue(
                                request
                            )
                    firebaseAsync.await()
                    Result.Success(Unit)
                }
            )
        } catch (e: Exception) {
            e(throwable = e, message = "Error adding request")
            Result.Error(e.toRemoteDataError())
        }
    }

    private suspend fun receiveUid(receiverEmail: String): Result<UID, DataError> =
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
                            FirebaseAuthException(
                                NO_USER_ERROR_CODE,
                                NO_USER_LOGGED_IN_MESSAGE
                            ),
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

    companion object {
        private const val NO_UID_FOUND = "no receiver uid found"
        private const val NO_USER_LOGGED_IN_MESSAGE = "no user logged in"
        private const val NO_USER_ERROR_CODE = "NO_USER"
    }

}