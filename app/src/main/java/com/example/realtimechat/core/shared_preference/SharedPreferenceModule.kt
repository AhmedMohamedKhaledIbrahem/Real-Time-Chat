package com.example.realtimechat.core.shared_preference

import android.content.Context
import android.content.SharedPreferences
import com.example.realtimechat.BuildConfig
import org.koin.dsl.module

val sharedPreferenceModule = module {
    single<SharedPreferences> {
        get<Context>().getSharedPreferences(BuildConfig.SharedPreferences, Context.MODE_PRIVATE)
    }
    single<RealTimeChatSharedPreference> {
        RealTimeChatSharedPreferenceImpl(get(), get())
    }
}