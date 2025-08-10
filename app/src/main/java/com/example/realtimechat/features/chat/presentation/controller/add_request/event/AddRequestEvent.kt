package com.example.realtimechat.features.chat.presentation.controller.add_request.event

sealed interface AddRequestEvent {
    data class SenderEmail(val email: String): AddRequestEvent
    data object AddRequestClicked : AddRequestEvent
}