package com.example.realtimechatapp.features.authentication.domain.repository

import com.example.realtimechatapp.core.error.AuthDataError
import com.example.realtimechatapp.core.error.AuthDomainError
import com.example.realtimechatapp.core.utils.Result
import com.example.realtimechatapp.features.authentication.domain.entity.SignUpEntity

interface AuthenticationRepository {
    suspend fun signIn(email:String , password:String): Result<Unit, AuthDomainError>
    suspend fun signUp(signUpParams: SignUpEntity): Result<Unit, AuthDomainError>
    suspend fun forgotPassword(email:String): Result<Unit, AuthDomainError>
}