package com.example.realtimechatapp

import android.app.Application
import com.example.realtimechatapp.core.firebase.firebaseModule
import com.example.realtimechatapp.core.firebase.initializeFirebase
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
            )
        }
    }
}