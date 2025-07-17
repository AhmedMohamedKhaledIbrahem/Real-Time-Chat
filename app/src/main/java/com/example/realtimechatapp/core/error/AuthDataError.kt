package com.example.realtimechatapp.core.error

sealed interface AuthDataError: Error {
    enum class Network: AuthDataError {
        AUTH_FAILED,
        USER_NOT_FOUND,
        USER_ALREADY_EXISTS,
        EMAIL_NOT_VERIFIED,
        NO_USER_LOGGED_IN,
        PERMISSION_DENIED,
        NO_USER_DATA_FOUND,
        NETWORK_UNAVAILABLE,
        TIMEOUT,
        UNKNOWN,

    }
    enum class Local : AuthDataError {
        DATABASE_ERROR,
        READ_FAILED,
        UNKNOWN
    }
}