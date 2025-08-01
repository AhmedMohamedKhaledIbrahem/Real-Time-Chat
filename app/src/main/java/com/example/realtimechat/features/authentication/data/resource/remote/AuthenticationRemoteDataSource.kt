package com.example.realtimechat.features.authentication.data.resource.remote

import com.example.realtimechat.BuildConfig
import com.example.realtimechat.core.error.DataError
import com.example.realtimechat.core.firebase.FirebaseInstance
import com.example.realtimechat.core.logger.Logger
import com.example.realtimechat.core.utils.Result
import com.example.realtimechat.features.authentication.data.mapper.toRemoteDataError
import com.example.realtimechat.features.authentication.data.mapper.toUserModel
import com.example.realtimechat.features.authentication.data.model.SignUpModel
import com.example.realtimechat.features.authentication.data.model.UserModel
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.tasks.await

interface AuthenticationRemoteDataSource {
    suspend fun signIn(
        email: String,
        password: String
    ): Result<Boolean, DataError>

    suspend fun signUp(signUpParams: SignUpModel): Result<String, DataError>
    suspend fun fetchUser(): Result<Pair<UserModel, String>, DataError>
    suspend fun forgotPassword(email: String): Result<Unit, DataError>
    suspend fun sendEmailVerification(): Result<Unit, DataError>
    suspend fun isLoggedIn(): Result<Boolean, DataError>
    suspend fun saveFcmToken(token: String): Result<Unit, DataError>
    suspend fun getFcmToken(): Result<String, DataError>
}

class AuthenticationRemoteDataSourceImpl(
    private val firebaseInstance: FirebaseInstance,
    logger: Logger
) : AuthenticationRemoteDataSource, Logger by logger {

    override suspend fun signIn(
        email: String,
        password: String
    ): Result<Boolean, DataError> {
        return try {
            val resultAsync =
                firebaseInstance.firebaseAuth().signInWithEmailAndPassword(email, password)
            val result = resultAsync.await()
            val user = result.user ?: run {
                e(
                    FirebaseAuthException(
                        AUTH_FAILED_ERROR_CODE,
                        USER_CREATION_MESSAGE
                    ),
                    USER_IS_NULL_MESSAGE
                )
                throw FirebaseAuthException(
                    AUTH_FAILED_ERROR_CODE,
                    USER_CREATION_MESSAGE
                )
            }
            Result.Success(user.isEmailVerified)
        } catch (e: Exception) {
            e(e, "Error signing in")
            Result.Error(e.toRemoteDataError())

        }
    }

    override suspend fun fetchUser(): Result<Pair<UserModel, String>, DataError> {
        return try {
            val user = firebaseInstance.firebaseAuth().currentUser
                ?: run {
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

            val uid = user.uid
            val userProfileAsync = firebaseInstance.firebaseDatabase()
                .getReference(BuildConfig.DB_REFERENCE)
                .child(uid)
                .get()
            val userProfile = userProfileAsync.await()
            val userModel = userProfile.getValue(UserModel::class.java)
                ?: run {
                    e(
                        FirebaseAuthException(
                            NO_USER_DATA_FOUND_CODE,
                            NO_USER_DATA_FOUND_MESSAGE
                        ),
                        NO_USER_DATA_FOUND_MESSAGE
                    )
                    throw FirebaseAuthException(
                        NO_USER_DATA_FOUND_CODE,
                        NO_USER_DATA_FOUND_MESSAGE
                    )
                }

            Result.Success(Pair(userModel, uid))

        } catch (e: Exception) {
            e(e, "Error fetching user")
            Result.Error(e.toRemoteDataError())
        }

    }

    override suspend fun signUp(signUpParams: SignUpModel): Result<String, DataError> {
        return try {
            val resultAsync = firebaseInstance.firebaseAuth().createUserWithEmailAndPassword(
                signUpParams.email,
                signUpParams.password
            )
            val result = resultAsync.await()
            val user = result.user ?: throw FirebaseAuthException(
                AUTH_FAILED_ERROR_CODE,
                USER_CREATION_MESSAGE
            )
            val uid = user.uid
            val firebaseAsync =
                firebaseInstance.firebaseDatabase().getReference(BuildConfig.DB_REFERENCE)
                    .child(uid).setValue(signUpParams.toUserModel())
            firebaseAsync.await()
            Result.Success(uid)
        } catch (e: Exception) {
            e(e, "Error signing up")
            Result.Error(e.toRemoteDataError())
        }
    }

    override suspend fun forgotPassword(email: String): Result<Unit, DataError> {
        return try {
            firebaseInstance.firebaseAuth().sendPasswordResetEmail(email).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            e(e, "Error sending password reset email")
            Result.Error(e.toRemoteDataError())
        }
    }

    override suspend fun sendEmailVerification(): Result<Unit, DataError> {
        return try {
            val user = firebaseInstance.firebaseAuth().currentUser
                ?: throw FirebaseAuthException(
                    NO_USER_ERROR_CODE,
                    NO_USER_LOGGED_IN_MESSAGE
                )
            user.sendEmailVerification().await()
            Result.Success(Unit)
        } catch (e: Exception) {
            e(e, "Error sending email verification")
            Result.Error(e.toRemoteDataError())
        }
    }

    override suspend fun isLoggedIn(): Result<Boolean, DataError> {
        return try {
            val user = firebaseInstance.firebaseAuth().currentUser != null
            Result.Success(user)
        } catch (e: Exception) {
            e(e, "Error checking if user is logged in")
            Result.Error(e.toRemoteDataError())
        }
    }

    override suspend fun saveFcmToken(token: String): Result<Unit, DataError> {
        return try {
            val user = firebaseInstance.firebaseAuth().currentUser ?: run {
                run {
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
            }
            val uid = user.uid
            val firebaseAsync = firebaseInstance.firebaseDatabase().getReference(
                BuildConfig.DB_REFERENCE
            ).child(uid).child(FCM).setValue(token)
            firebaseAsync.await()
            Result.Success(Unit)
        } catch (e: Exception) {
            e(e, ERROR_SAVE_FCM_TOKEN_CODE)
            Result.Error(e.toRemoteDataError())
        }
    }

    override suspend fun getFcmToken(): Result<String, DataError> {
        return try {
            val resultAsync = firebaseInstance.firebaseMessaging().token
            val result = resultAsync.await()
            Result.Success(result)
        } catch (e: Exception) {
            e(e, ERROR_GETTING_FCM_TOKEN_CODE)
            Result.Error(e.toRemoteDataError())
        }
    }

    companion object {
        private const val FCM = "Fcm"
        private const val NO_USER_DATA_FOUND_MESSAGE = "no user data found"
        private const val NO_USER_LOGGED_IN_MESSAGE = "no user logged in"
        private const val USER_CREATION_MESSAGE = "user creation failed"
        private const val USER_IS_NULL_MESSAGE = "User is null after sign-in"
        private const val NO_USER_ERROR_CODE = "NO_USER"
        private const val AUTH_FAILED_ERROR_CODE = "AUTH_FAILED"
        private const val NO_USER_DATA_FOUND_CODE = "NO_USER_DATA_FOUND"
        private const val ERROR_GETTING_FCM_TOKEN_CODE = "ERROR_GETTING_FCM_TOKEN"
        private const val ERROR_SAVE_FCM_TOKEN_CODE = "ERROR_SAVE_FCM_TOKEN"
    }

}