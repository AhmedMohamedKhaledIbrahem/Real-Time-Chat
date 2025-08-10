package com.example.realtimechat.features.chat.module

import com.example.realtimechat.features.chat.data.repository.request.AddRequestRepositoryImpl
import com.example.realtimechat.features.chat.data.source.local.AddRequestLocalDataSource
import com.example.realtimechat.features.chat.data.source.local.AddRequestLocalDataSourceImpl
import com.example.realtimechat.features.chat.data.source.remote.request.AddRequestRemoteDataSource
import com.example.realtimechat.features.chat.data.source.remote.request.AddRequestRemoteDataSourceImpl
import com.example.realtimechat.features.chat.domain.repository.request.AddRequestRepository
import com.example.realtimechat.features.chat.domain.usecase.add_request.AddRequestUseCase
import com.example.realtimechat.features.chat.domain.usecase.add_request.AddRequestUseCaseImpl
import com.example.realtimechat.features.chat.domain.usecase.get_fcm_token_by_email.GetFcmTokenByEmailUseCase
import com.example.realtimechat.features.chat.domain.usecase.get_fcm_token_by_email.GetFcmTokenByEmailUseCaseImpl
import com.example.realtimechat.features.chat.domain.usecase.send_fcm_request.SendFcmRequestUseCase
import com.example.realtimechat.features.chat.domain.usecase.send_fcm_request.SendFcmRequestUseCaseImpl
import com.example.realtimechat.features.chat.presentation.controller.add_request.AddRequestViewModel
import com.example.realtimechat.features.chat.presentation.controller.fcm.FcmViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val chatModule = module {
    single<AddRequestRemoteDataSource> {
        AddRequestRemoteDataSourceImpl(
            get(),
            get(),
            get()
        )
    }
    single<AddRequestLocalDataSource> {
        AddRequestLocalDataSourceImpl(
            get(),
            get()
        )
    }
    single<AddRequestRepository> {
        AddRequestRepositoryImpl(
            get(),
            get(),
            get()
        )
    }
    single<AddRequestUseCase> { AddRequestUseCaseImpl(get()) }
    single<SendFcmRequestUseCase> { SendFcmRequestUseCaseImpl(get()) }
    single<GetFcmTokenByEmailUseCase> { GetFcmTokenByEmailUseCaseImpl(get()) }
    viewModel { AddRequestViewModel(get(), get()) }
    viewModel { FcmViewModel(get(), get(), get()) }

}