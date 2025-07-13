package com.example.realtimechatapp.core.event

import com.example.realtimechatapp.core.ui_text.UiText


inline fun<T> combinedEvent(
    event: List<UiEvent>,
    crossinline onShowMessage: (message: UiText) -> Unit,
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