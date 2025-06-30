package com.example.realtimechatapp.core.firebase

import android.content.Context
import com.google.firebase.FirebaseApp

fun initializeFirebase(context: Context){
    FirebaseApp.initializeApp(context)
}