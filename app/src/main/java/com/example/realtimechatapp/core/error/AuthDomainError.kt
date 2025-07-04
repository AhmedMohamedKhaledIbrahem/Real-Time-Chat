package com.example.realtimechatapp.core.error

sealed interface AuthDomainError: Error {
    enum class Network: AuthDomainError {
        AUTH_FAILED,
        USER_NOT_FOUND,
        USER_ALREADY_EXISTS,
        EMAIL_NOT_VERIFIED,
        PERMISSION_DENIED,
        NETWORK_UNAVAILABLE,
        TIMEOUT,
        UNKNOWN,

    }
    enum class Local : AuthDomainError {
        DATABASE_ERROR,
        READ_FAILED,
        UNKNOWN
    }
}