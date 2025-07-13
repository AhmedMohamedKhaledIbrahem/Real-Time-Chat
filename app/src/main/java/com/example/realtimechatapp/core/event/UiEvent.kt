package com.example.realtimechatapp.core.event

import androidx.navigation3.runtime.NavKey
import com.example.realtimechatapp.core.ui_text.UiText
import kotlinx.serialization.Serializable


typealias NavigateEvent = UiEvent.NavEvent
typealias ShowSnackBarEvent = UiEvent.ShowSnackBar
typealias CombinedEvents = UiEvent.CombinedEvents
sealed interface UiEvent {
    sealed interface NavEvent : UiEvent, NavKey{
        @Serializable
        data object SignUpScreen : NavEvent
        @Serializable
        data object SignInScreen: NavEvent
        @Serializable
        data object ForgetPasswordScreen: NavEvent
        @Serializable
        data object HomeScreen: NavEvent

    }
    data class  ShowSnackBar(val message: UiText) : UiEvent
    data class CombinedEvents(val event:List<UiEvent>): UiEvent
}

