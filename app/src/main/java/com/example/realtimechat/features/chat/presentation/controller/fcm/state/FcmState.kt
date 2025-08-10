package com.example.realtimechat.features.chat.presentation.controller.fcm.state

data class FcmState(
    val isLoading: Boolean = false,
    val title: String? = null,
    val body: String? = null,
    val email: String? = null,

    )
