package com.example.realtimechat.features.authentication.presentation.controller.forgetpassword.event

sealed interface ForgetPasswordEvent {
    data class EmailInput(val email: String) : ForgetPasswordEvent
    data object ForgetPasswordClick : ForgetPasswordEvent
}