package com.example.realtimechatapp.features.authentication.presentation.controller.signup.state

data class SignUpState(
    val isLoading: Boolean = false,
    val name: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val password: String? = null,
)
