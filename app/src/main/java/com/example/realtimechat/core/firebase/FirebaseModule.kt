package com.example.realtimechat.core.firebase

import org.koin.dsl.module

val firebaseModule = module {
    single <FirebaseInstance> { FirebaseInstanceImpl() }
}