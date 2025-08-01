package com.example.realtimechat.features.chat.module

import com.example.realtimechat.features.chat.data.source.remote.request.AddRequestRemoteDataSource
import com.example.realtimechat.features.chat.data.source.remote.request.AddRequestRemoteDataSourceImpl
import org.koin.dsl.module

val chatModule = module {
    single<AddRequestRemoteDataSource> { AddRequestRemoteDataSourceImpl(get(), get()) }

}