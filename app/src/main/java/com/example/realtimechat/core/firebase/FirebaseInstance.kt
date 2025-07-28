package com.example.realtimechat.core.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

interface FirebaseInstance{
     fun firebaseDatabase(): FirebaseDatabase
     fun firebaseAuth(): FirebaseAuth
}

class FirebaseInstanceImpl: FirebaseInstance {
    override  fun firebaseDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }
    override  fun firebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
}
