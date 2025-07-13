package com.example.realtimechatapp.features.authentication.presentation.controller.signin.event

sealed interface SignInEvent {
    data class Email(val email: String) : SignInEvent
    data class Password(val password: String) : SignInEvent
    sealed interface Click: SignInEvent{
        data object SignIn: SignInEvent
        data object SignUp: SignInEvent
        data object ForgetPassword: SignInEvent
    }

}