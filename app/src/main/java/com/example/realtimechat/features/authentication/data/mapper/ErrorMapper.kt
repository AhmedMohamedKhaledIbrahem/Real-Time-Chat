package com.example.realtimechat.features.authentication.data.mapper

import com.example.realtimechat.core.error.DataError
import com.example.realtimechat.core.error.DomainError

fun DataError.Network.toDomainError(): DomainError.Network = when (this) {
    DataError.Network.AUTH_FAILED -> DomainError.Network.AUTH_FAILED
    DataError.Network.USER_NOT_FOUND -> DomainError.Network.USER_NOT_FOUND
    DataError.Network.USER_ALREADY_EXISTS -> DomainError.Network.USER_ALREADY_EXISTS
    DataError.Network.EMAIL_NOT_VERIFIED -> DomainError.Network.EMAIL_NOT_VERIFIED
    DataError.Network.PERMISSION_DENIED -> DomainError.Network.PERMISSION_DENIED
    DataError.Network.NETWORK_UNAVAILABLE -> DomainError.Network.NETWORK_UNAVAILABLE
    DataError.Network.TIMEOUT -> DomainError.Network.TIMEOUT
    DataError.Network.NO_USER_LOGGED_IN -> DomainError.Network.NO_USER_LOGGED_IN
    DataError.Network.NO_USER_DATA_FOUND -> DomainError.Network.NO_USER_DATA_FOUND
    DataError.Network.UNKNOWN -> DomainError.Network.UNKNOWN

}
fun DataError.Local.toDomainError(): DomainError.Local = when (this) {
    DataError.Local.DATABASE_ERROR -> DomainError.Local.DATABASE_ERROR
    DataError.Local.READ_FAILED -> DomainError.Local.READ_FAILED
    DataError.Local.UNKNOWN -> DomainError.Local.UNKNOWN
}

fun DataError.toDomainError(): DomainError = when (this) {
    is DataError.Network -> this.toDomainError()
    is DataError.Local -> this.toDomainError()
}