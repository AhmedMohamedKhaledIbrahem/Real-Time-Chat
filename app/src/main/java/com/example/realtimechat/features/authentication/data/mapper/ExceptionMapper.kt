package com.example.realtimechat.features.authentication.data.mapper

import android.database.sqlite.SQLiteException
import com.example.realtimechat.core.error.AuthDataError

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import java.io.IOException

fun Exception.toRemoteDataError(): AuthDataError = when (this) {
    is FirebaseAuthInvalidCredentialsException -> AuthDataError.Network.AUTH_FAILED
    is FirebaseAuthInvalidUserException -> AuthDataError.Network.USER_NOT_FOUND
    is FirebaseAuthUserCollisionException -> AuthDataError.Network.USER_ALREADY_EXISTS
    is FirebaseNetworkException -> AuthDataError.Network.NETWORK_UNAVAILABLE
    is FirebaseAuthException -> {
        when(this.errorCode){
            "AUTH_FAILED" -> AuthDataError.Network.AUTH_FAILED
            "NO_USER" -> AuthDataError.Network.NO_USER_LOGGED_IN
            "NO_USER_DATA_FOUND" -> AuthDataError.Network.NO_USER_DATA_FOUND
            else -> AuthDataError.Network.UNKNOWN
        }
    }
    is FirebaseTooManyRequestsException -> AuthDataError.Network.TIMEOUT
    else -> AuthDataError.Network.UNKNOWN
}

fun Exception.toLocalDataError(): AuthDataError.Local = when (this) {
    is SQLiteException -> AuthDataError.Local.DATABASE_ERROR
    is IOException -> AuthDataError.Local.READ_FAILED
    else -> AuthDataError.Local.UNKNOWN
}