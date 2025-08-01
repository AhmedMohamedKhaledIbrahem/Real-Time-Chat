package com.example.realtimechat.features.chat.data.model

enum class AddRequestStatus {
    PENDING,
    ACCEPTED,
    REJECTED
}

data class AddRequestModel(
    val senderEmail: String,
    val receiverEmail: String,
    val status: AddRequestStatus = AddRequestStatus.PENDING,
    val timestamp: Long
)