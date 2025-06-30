package com.example.realtimechatapp.core.error

sealed interface DataError: Error {
    enum class Network: DataError {
        REQUEST_TIMEOUT,
        NO_CONNECTION,
        SERVER_ERROR,
        UNKNOWN_ERROR,
    }
    enum class Local : DataError {
        FILE_NOT_FOUND,
        SQL_ERROR,
        UNKNOWN_ERROR,
    }
}