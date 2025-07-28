package com.example.realtimechat.features.authentication.domain.repository

import com.example.realtimechat.core.error.AuthDomainError
import com.example.realtimechat.core.utils.Result
import com.example.realtimechat.features.authentication.domain.entity.SignUpEntity

interface AuthenticationRepository {
    suspend fun signIn(email:String , password:String): Result<Unit, AuthDomainError>
    suspend fun sendVerificationEmail(isVerified:Boolean): Result<Unit, AuthDomainError>
    suspend fun saveUser(isVerified: Boolean): Result<Unit, AuthDomainError>
    suspend fun activeUserByEmail(email: String): Result<Unit, AuthDomainError>
    suspend fun signUp(signUpParams: SignUpEntity): Result<Unit, AuthDomainError>
    suspend fun forgotPassword(email:String): Result<Unit, AuthDomainError>
    suspend fun isLoggedIn():Result<Boolean, AuthDomainError>
}