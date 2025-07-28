package com.example.realtimechat

import android.app.Application
import com.example.realtimechat.core.database.di.databaseModule
import com.example.realtimechat.core.firebase.firebaseModule
import com.example.realtimechat.core.firebase.initializeFirebase
import com.example.realtimechat.core.logger.loggerModule
import com.example.realtimechat.core.module.networkModule
import com.example.realtimechat.features.authentication.module.authenticationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeFirebase(context = this)
        startKoin {
            androidContext(this@MyApp)
            modules(
                firebaseModule,
                authenticationModule,
                databaseModule,
                networkModule,
                loggerModule,
            )
        }
    }
}