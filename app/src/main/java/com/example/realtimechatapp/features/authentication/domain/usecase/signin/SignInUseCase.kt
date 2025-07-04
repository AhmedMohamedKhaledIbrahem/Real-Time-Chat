package com.example.realtimechatapp.features.authentication.domain.usecase.signin

import com.example.realtimechatapp.features.authentication.domain.repository.AuthenticationRepository

interface SignInUseCase {
    suspend operator fun invoke(email: String, password: String)
}

class SignInUseCaseImpl(private val repository: AuthenticationRepository) : SignInUseCase {
    override suspend fun invoke(email: String, password: String) {
        repository.signIn(email, password)
    }

}