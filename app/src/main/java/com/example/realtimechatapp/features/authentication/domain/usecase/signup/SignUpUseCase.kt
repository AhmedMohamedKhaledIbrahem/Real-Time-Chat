package com.example.realtimechatapp.features.authentication.domain.usecase.signup

import com.example.realtimechatapp.features.authentication.domain.entity.SignUpEntity
import com.example.realtimechatapp.features.authentication.domain.repository.AuthenticationRepository

interface SignUpUseCase {
    suspend operator fun invoke(signUpParams: SignUpEntity)
}
class  SignUpUseCaseImpl(private val repository: AuthenticationRepository) : SignUpUseCase {
    override suspend fun invoke(signUpParams: SignUpEntity) {
        repository.signUp(signUpParams)
    }
}