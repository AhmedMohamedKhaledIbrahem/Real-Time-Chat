package com.example.realtimechatapp.core.error

sealed interface DomainError: Error {
    enum class Network: DomainError {

        NETWORK_UNAVAILABLE,
        TIMEOUT,
        UNKNOWN,

    }
    enum class Auth : DomainError {
        AUTH_FAILED,
        USER_NOT_FOUND,
        USER_ALREADY_EXISTS,
        EMAIL_NOT_VERIFIED,
        PERMISSION_DENIED,
    }
    enum class Local : DomainError {
        DATABASE_ERROR,
        READ_FAILED,
        UNKNOWN
    }
}