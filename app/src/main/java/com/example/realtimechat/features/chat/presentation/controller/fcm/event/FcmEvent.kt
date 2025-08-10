package com.example.realtimechat.features.chat.presentation.controller.fcm.event

sealed interface FcmEvent {
    data class Title(val title: String) : FcmEvent
    data class Body(val body: String) : FcmEvent
    data class Email(val email: String) : FcmEvent
    data object FcmClick : FcmEvent

}