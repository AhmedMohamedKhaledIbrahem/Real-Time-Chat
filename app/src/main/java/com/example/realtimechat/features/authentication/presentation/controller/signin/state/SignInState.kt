package com.example.realtimechat.features.authentication.presentation.controller.signin.state

data class SignInState(
    val isLoading: Boolean = false,
    val user: String? = null,
    val password: String? = null,
)
