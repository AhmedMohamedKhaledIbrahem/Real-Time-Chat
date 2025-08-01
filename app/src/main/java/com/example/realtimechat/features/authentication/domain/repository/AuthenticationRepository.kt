package com.example.realtimechat.features.authentication.domain.repository

import com.example.realtimechat.core.error.DomainError
import com.example.realtimechat.core.utils.Result
import com.example.realtimechat.features.authentication.domain.entity.SignUpEntity

interface AuthenticationRepository {
    suspend fun signIn(email:String , password:String): Result<Unit, DomainError>
    suspend fun sendVerificationEmail(isVerified:Boolean): Result<Unit, DomainError>
    suspend fun saveUser(isVerified: Boolean): Result<Unit, DomainError>
    suspend fun activeUserByEmail(email: String): Result<Unit, DomainError>
    suspend fun signUp(signUpParams: SignUpEntity): Result<Unit, DomainError>
    suspend fun forgotPassword(email:String): Result<Unit, DomainError>
    suspend fun isLoggedIn():Result<Boolean, DomainError>
    suspend fun getFcmToken(): Result<String, DomainError>
    suspend fun saveFcmToken(): Result<Unit, DomainError>
}