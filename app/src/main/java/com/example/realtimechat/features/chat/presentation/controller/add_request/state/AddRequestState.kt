package com.example.realtimechat.features.chat.presentation.controller.add_request.state

data class AddRequestState(
    val isLoading: Boolean = false,
    val receiverEmail: String? = null,
    val senderEmail: String? = null,
)
