package com.example.realtimechat.core.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging

interface FirebaseInstance{
     fun firebaseDatabase(): FirebaseDatabase
     fun firebaseAuth(): FirebaseAuth
     fun firebaseMessaging(): FirebaseMessaging
}

class FirebaseInstanceImpl: FirebaseInstance {
    override  fun firebaseDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }
    override  fun firebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    override fun firebaseMessaging(): FirebaseMessaging {
        return FirebaseMessaging.getInstance()
    }
}
