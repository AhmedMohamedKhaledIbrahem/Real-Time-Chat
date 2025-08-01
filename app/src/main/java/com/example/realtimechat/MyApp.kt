package com.example.realtimechat

import android.app.Application
import com.example.realtimechat.core.database.di.databaseModule
import com.example.realtimechat.core.firebase.firebaseModule
import com.example.realtimechat.core.firebase.initializeFirebase
import com.example.realtimechat.core.logger.loggerModule
import com.example.realtimechat.core.module.networkModule
import com.example.realtimechat.core.notification.module.realTimeChatNotificationModule
import com.example.realtimechat.core.shared_preference.sharedPreferenceModule
import com.example.realtimechat.features.authentication.module.authenticationModule
import com.example.realtimechat.features.chat.module.chatModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber


class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeFirebase(context = this)
        startKoin {
            androidContext(this@MyApp)
            modules(
                firebaseModule,
                authenticationModule,
                chatModule,
                databaseModule,
                networkModule,
                realTimeChatNotificationModule,
                sharedPreferenceModule,
                loggerModule,
            )
        }
        if (BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
    }
}