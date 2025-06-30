package com.example.realtimechatapp.core.firebase

import org.koin.dsl.module

val firebaseModule = module {
    single <FirebaseInstance> { FirebaseInstanceImpl() }
}