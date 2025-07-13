package com.example.realtimechatapp.features.authentication.module

import com.example.realtimechatapp.features.authentication.data.repository.AuthenticationRepositoryImpl
import com.example.realtimechatapp.features.authentication.data.resource.local.AuthenticationLocalDataSource
import com.example.realtimechatapp.features.authentication.data.resource.local.AuthenticationLocalDataSourceImpl
import com.example.realtimechatapp.features.authentication.data.resource.remote.AuthenticationRemoteDataSource
import com.example.realtimechatapp.features.authentication.data.resource.remote.AuthenticationRemoteDataSourceImpl
import com.example.realtimechatapp.features.authentication.domain.repository.AuthenticationRepository
import com.example.realtimechatapp.features.authentication.domain.usecase.forgetpassword.ForgetPasswordUseCase
import com.example.realtimechatapp.features.authentication.domain.usecase.forgetpassword.ForgetPasswordUseCaseImpl
import com.example.realtimechatapp.features.authentication.domain.usecase.signin.SignInUseCase
import com.example.realtimechatapp.features.authentication.domain.usecase.signin.SignInUseCaseImpl
import com.example.realtimechatapp.features.authentication.domain.usecase.signup.SignUpUseCase
import com.example.realtimechatapp.features.authentication.domain.usecase.signup.SignUpUseCaseImpl
import com.example.realtimechatapp.features.authentication.domain.validator.EmailValidator
import com.example.realtimechatapp.features.authentication.domain.validator.RegexEmailValidator
import org.koin.dsl.module

val authenticationModule = module {
    single<AuthenticationRemoteDataSource> { AuthenticationRemoteDataSourceImpl(get()) }
    single<AuthenticationLocalDataSource> { AuthenticationLocalDataSourceImpl(get()) }
    single<AuthenticationRepository> { AuthenticationRepositoryImpl(get(), get(), get()) }
    single<EmailValidator> { RegexEmailValidator() }
    single<SignInUseCase> { SignInUseCaseImpl(get(), get()) }
    single<SignUpUseCase> { SignUpUseCaseImpl(get()) }
    single<ForgetPasswordUseCase> { ForgetPasswordUseCaseImpl(get()) }
}