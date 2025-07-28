package com.example.realtimechat.features.authentication.module

import com.example.realtimechat.features.authentication.data.repository.AuthenticationRepositoryImpl
import com.example.realtimechat.features.authentication.data.resource.local.AuthenticationLocalDataSource
import com.example.realtimechat.features.authentication.data.resource.local.AuthenticationLocalDataSourceImpl
import com.example.realtimechat.features.authentication.data.resource.remote.AuthenticationRemoteDataSource
import com.example.realtimechat.features.authentication.data.resource.remote.AuthenticationRemoteDataSourceImpl
import com.example.realtimechat.features.authentication.domain.repository.AuthenticationRepository
import com.example.realtimechat.features.authentication.domain.usecase.forgetpassword.ForgetPasswordUseCase
import com.example.realtimechat.features.authentication.domain.usecase.forgetpassword.ForgetPasswordUseCaseImpl
import com.example.realtimechat.features.authentication.domain.usecase.is_logged.IsLoggedUseCase
import com.example.realtimechat.features.authentication.domain.usecase.is_logged.IsLoggedUseCaseImpl
import com.example.realtimechat.features.authentication.domain.usecase.signin.SignInUseCase
import com.example.realtimechat.features.authentication.domain.usecase.signin.SignInUseCaseImpl
import com.example.realtimechat.features.authentication.domain.usecase.signup.SignUpUseCase
import com.example.realtimechat.features.authentication.domain.usecase.signup.SignUpUseCaseImpl
import com.example.realtimechat.features.authentication.domain.validator.Validator
import com.example.realtimechat.features.authentication.domain.validator.ValidatorImpl
import com.example.realtimechat.features.authentication.presentation.controller.forgetpassword.ForgetPasswordViewModel
import com.example.realtimechat.features.authentication.presentation.controller.is_logged.IsLoggedViewModel
import com.example.realtimechat.features.authentication.presentation.controller.signin.SignInViewModel
import com.example.realtimechat.features.authentication.presentation.controller.signup.SignUpViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val authenticationModule = module {
    single<AuthenticationRemoteDataSource> { AuthenticationRemoteDataSourceImpl(get(), get()) }
    single<AuthenticationLocalDataSource> { AuthenticationLocalDataSourceImpl(get(), get()) }
    single<AuthenticationRepository> { AuthenticationRepositoryImpl(get(), get(), get()) }
    single<Validator> { ValidatorImpl() }
    single<SignInUseCase> { SignInUseCaseImpl(get(), get()) }
    single<SignUpUseCase> { SignUpUseCaseImpl(get()) }
    single<ForgetPasswordUseCase> { ForgetPasswordUseCaseImpl(get()) }
    single<IsLoggedUseCase>{ IsLoggedUseCaseImpl(get()) }
    viewModel { SignInViewModel(get()) }
    viewModel { SignUpViewModel(get(), get()) }
    viewModel { ForgetPasswordViewModel(get()) }
    viewModel { IsLoggedViewModel(get()) }
}