package com.example.realtimechat.features.authentication.presentation.controller.signup.event

sealed interface SignUpEvent {
    sealed interface Click: SignUpEvent {
       data object SignUp: Click
       data object SignIn: Click
    }
    sealed interface Input: SignUpEvent {
        data class Name(val name: String): Input
        data class Email(val email: String): Input
        data class Phone(val phone: String): Input
        data class Password(val password: String): Input
    }
}