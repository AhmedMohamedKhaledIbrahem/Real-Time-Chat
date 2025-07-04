package com.example.realtimechatapp.features.authentication.data.mapper

import android.database.sqlite.SQLiteException
import com.example.realtimechatapp.core.error.AuthDataError

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
    is FirebaseAuthException -> AuthDataError.Network.PERMISSION_DENIED
    is FirebaseTooManyRequestsException -> AuthDataError.Network.TIMEOUT
    else -> AuthDataError.Network.UNKNOWN
}

fun Exception.toLocalDataError(): AuthDataError.Local = when (this) {
    is SQLiteException -> AuthDataError.Local.DATABASE_ERROR
    is IOException -> AuthDataError.Local.READ_FAILED
    else -> AuthDataError.Local.UNKNOWN
}