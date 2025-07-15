package com.example.realtimechatapp.features.authentication.data.resource.remote

import com.example.realtimechatapp.core.error.AuthDataError
import com.example.realtimechatapp.core.firebase.FirebaseInstance
import com.example.realtimechatapp.core.utils.Result
import com.example.realtimechatapp.features.authentication.data.mapper.toRemoteDataError
import com.example.realtimechatapp.features.authentication.data.model.SignUpModel
import kotlinx.coroutines.tasks.await
import com.example.realtimechatapp.BuildConfig
import com.google.firebase.auth.FirebaseAuthException

interface AuthenticationRemoteDataSource {
    suspend fun signIn(
        email: String,
        password: String
    ): Result<Boolean, AuthDataError>

    suspend fun signUp(signUpParams: SignUpModel): Result<String, AuthDataError>

    suspend fun forgotPassword(email: String): Result<Unit, AuthDataError>
    suspend fun sendEmailVerification(): Result<Unit, AuthDataError>
}

class AuthenticationRemoteDataSourceImpl(
    private val firebaseInstance: FirebaseInstance
) : AuthenticationRemoteDataSource {

    override suspend fun signIn(
        email: String,
        password: String
    ): Result<Boolean, AuthDataError> {
        return try {
            val result =
                firebaseInstance.firebaseAuth().signInWithEmailAndPassword(email, password).await()
            val user = result.user ?: throw FirebaseAuthException(
                "AUTH_FAILED",
                "User creation failed"
            )
            Result.Success(user.isEmailVerified)
        } catch (e: Exception) {
            Result.Error(e.toRemoteDataError())

        }
    }

    override suspend fun fetchUser(): Result<UserModel, AuthDataError> {
        return try {
            val user = firebaseInstance.firebaseAuth().currentUser
                ?: throw FirebaseAuthException("NO_USER", "No user logged in")

            val uid = user.uid
            val userProfileAsync = firebaseInstance.firebaseDatabase()
                .getReference(BuildConfig.DB_REFERENCE)
                .child(uid)
                .get()
            val userProfile = userProfileAsync.await()
            val userModel = userProfile.getValue(UserModel::class.java)
                ?: throw FirebaseAuthException("NO_USER", "No user logged in")

            Result.Success( userModel)

        } catch (e: Exception) {
            Result.Error(e.toRemoteDataError())
        }

    }

    override suspend fun signUp(signUpParams: SignUpModel): Result<String, AuthDataError> {
        return try {
            val result = firebaseInstance.firebaseAuth().createUserWithEmailAndPassword(
                signUpParams.email,
                signUpParams.password
            ).await()
            val user = result.user ?: throw FirebaseAuthException(
                "AUTH_FAILED",
                "User creation failed"
            )
            val uid = user.uid
            firebaseInstance.firebaseDatabase().getReference(BuildConfig.DB_REFERENCE)
                .child(uid).setValue(signUpParams).await()
            Result.Success(uid)
        } catch (e: Exception) {
            Result.Error(e.toRemoteDataError())
        }
    }

    override suspend fun forgotPassword(email: String): Result<Unit, AuthDataError> {
        return try {
            firebaseInstance.firebaseAuth().sendPasswordResetEmail(email).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.toRemoteDataError())
        }
    }

    override suspend fun sendEmailVerification(): Result<Unit, AuthDataError> {
        return try {
            val user = firebaseInstance.firebaseAuth().currentUser
                ?: throw FirebaseAuthException("NO_USER", "No user logged in")
            user.sendEmailVerification().await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.toRemoteDataError())
        }
    }

}