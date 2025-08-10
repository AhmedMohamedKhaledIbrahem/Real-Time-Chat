package com.example.realtimechat.core.error

sealed interface DataError: Error {
    enum class Network: DataError {
        AUTH_FAILED,
        USER_NOT_FOUND,
        USER_ALREADY_EXISTS,
        EMAIL_NOT_VERIFIED,
        NO_USER_LOGGED_IN,
        PERMISSION_DENIED,
        NO_USER_DATA_FOUND,
        NETWORK_UNAVAILABLE,
        TIMEOUT,
        SERIALIZATION,
        DATABASE_ERROR,
        UNKNOWN,

    }
    enum class Local : DataError {
        DATABASE_ERROR,
        READ_FAILED,
        UNKNOWN
    }
}