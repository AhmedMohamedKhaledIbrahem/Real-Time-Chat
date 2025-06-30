package com.example.realtimechatapp.core.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

interface FirebaseInstance{
    suspend fun firebaseDatabase(): FirebaseDatabase
    suspend fun firebaseAuth(): FirebaseAuth
}

class FirebaseInstanceImpl: FirebaseInstance {
    override suspend fun firebaseDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }
    override suspend fun firebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
}
