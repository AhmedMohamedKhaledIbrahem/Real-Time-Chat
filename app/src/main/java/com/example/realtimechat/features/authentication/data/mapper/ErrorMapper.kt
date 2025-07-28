package com.example.realtimechat.features.authentication.data.mapper

import com.example.realtimechat.core.error.AuthDataError
import com.example.realtimechat.core.error.AuthDomainError

fun AuthDataError.Network.toDomainError(): AuthDomainError.Network = when (this) {
    AuthDataError.Network.AUTH_FAILED -> AuthDomainError.Network.AUTH_FAILED
    AuthDataError.Network.USER_NOT_FOUND -> AuthDomainError.Network.USER_NOT_FOUND
    AuthDataError.Network.USER_ALREADY_EXISTS -> AuthDomainError.Network.USER_ALREADY_EXISTS
    AuthDataError.Network.EMAIL_NOT_VERIFIED -> AuthDomainError.Network.EMAIL_NOT_VERIFIED
    AuthDataError.Network.PERMISSION_DENIED -> AuthDomainError.Network.PERMISSION_DENIED
    AuthDataError.Network.NETWORK_UNAVAILABLE -> AuthDomainError.Network.NETWORK_UNAVAILABLE
    AuthDataError.Network.TIMEOUT -> AuthDomainError.Network.TIMEOUT
    AuthDataError.Network.NO_USER_LOGGED_IN -> AuthDomainError.Network.NO_USER_LOGGED_IN
    AuthDataError.Network.NO_USER_DATA_FOUND -> AuthDomainError.Network.NO_USER_DATA_FOUND
    AuthDataError.Network.UNKNOWN -> AuthDomainError.Network.UNKNOWN

}
fun AuthDataError.Local.toDomainError(): AuthDomainError.Local = when (this) {
    AuthDataError.Local.DATABASE_ERROR -> AuthDomainError.Local.DATABASE_ERROR
    AuthDataError.Local.READ_FAILED -> AuthDomainError.Local.READ_FAILED
    AuthDataError.Local.UNKNOWN -> AuthDomainError.Local.UNKNOWN
}

fun AuthDataError.toDomainError(): AuthDomainError = when (this) {
    is AuthDataError.Network -> this.toDomainError()
    is AuthDataError.Local -> this.toDomainError()
}