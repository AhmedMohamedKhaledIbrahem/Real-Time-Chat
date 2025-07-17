package com.example.realtimechatapp.core.error

sealed interface AuthDomainError : Error {
    enum class Network : AuthDomainError {
        AUTH_FAILED,
        USER_NOT_FOUND,
        USER_ALREADY_EXISTS,
        NO_USER_LOGGED_IN,
        EMAIL_NOT_VERIFIED,
        PERMISSION_DENIED,
        NETWORK_UNAVAILABLE,
        NO_USER_DATA_FOUND,
        INVALID_SIGN_UP_PARAMS,
        INVALID_LOGIN_PARAMS,
        INVALID_EMAIL,
        TIMEOUT,
        UNKNOWN,

    }

    enum class Local : AuthDomainError {
        DATABASE_ERROR,
        READ_FAILED,
        UNKNOWN
    }
}