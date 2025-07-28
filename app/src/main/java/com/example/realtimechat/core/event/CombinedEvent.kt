package com.example.realtimechat.core.event

import com.example.realtimechat.core.ui_text.UiText


suspend inline fun combinedEvent(
    event: List<UiEvent>,
    crossinline onShowMessage: suspend (message: UiText) -> Unit,
    crossinline onNavigate: (route: NavigateEvent) -> Unit
) {
    event.forEach { event ->
        when (event) {
            is ShowSnackBarEvent -> onShowMessage(event.message)
            is NavigateEvent -> onNavigate(event)
            else -> Unit
        }
    }
}