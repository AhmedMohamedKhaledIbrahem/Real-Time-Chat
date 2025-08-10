package com.example.realtimechat.core.error

sealed interface DomainError : Error {
    enum class Network : DomainError {
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
        SERIALIZATION,
        DATABASE_ERROR,
        UNKNOWN,

    }

    enum class Local : DomainError {
        DATABASE_ERROR,
        READ_FAILED,
        UNKNOWN
    }

    enum class Password:DomainError{
        TOO_SHORT,
        NO_DIGIT,
        NO_SPECIAL_CHARACTER,
        NO_UPPERCASE,
        NO_LOWERCASE,
    }
    enum class Email:DomainError{
        INVALID_EMAIL,
    }
    enum class Name:DomainError{
        INVALID_NAME,
    }
    enum class Phone:DomainError{
        INVALID_PHONE,
    }

}