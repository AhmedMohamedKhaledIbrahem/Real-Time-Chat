package com.example.realtimechat.core.utils

import android.database.sqlite.SQLiteException
import com.example.realtimechat.core.error.DataError

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.DatabaseException
import kotlinx.serialization.SerializationException
import java.io.IOException

fun Exception.toRemoteDataError(): DataError = when (this) {
    is FirebaseAuthInvalidCredentialsException -> DataError.Network.AUTH_FAILED
    is FirebaseAuthInvalidUserException -> DataError.Network.USER_NOT_FOUND
    is FirebaseAuthUserCollisionException -> DataError.Network.USER_ALREADY_EXISTS
    is FirebaseNetworkException -> DataError.Network.NETWORK_UNAVAILABLE
    is FirebaseAuthException -> {
        when(this.errorCode){
            "AUTH_FAILED" -> DataError.Network.AUTH_FAILED
            "NO_USER" -> DataError.Network.NO_USER_LOGGED_IN
            "NO_USER_DATA_FOUND" -> DataError.Network.NO_USER_DATA_FOUND
            else -> DataError.Network.UNKNOWN
        }
    }
    is DatabaseException -> DataError.Network.DATABASE_ERROR
    is FirebaseTooManyRequestsException -> DataError.Network.TIMEOUT
    is SerializationException -> DataError.Network.SERIALIZATION
    else -> DataError.Network.UNKNOWN
}

fun Exception.toLocalDataError(): DataError.Local = when (this) {
    is SQLiteException -> DataError.Local.DATABASE_ERROR
    is IOException -> DataError.Local.READ_FAILED
    else -> DataError.Local.UNKNOWN
}