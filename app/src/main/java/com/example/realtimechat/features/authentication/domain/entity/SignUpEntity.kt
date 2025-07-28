package com.example.realtimechat.features.authentication.domain.entity

data class SignUpEntity(
    val name: String,
    val email: String,
    val phoneNumber: String,
    val password: String,
) {
    fun isValid(): Boolean {
        return name.isBlank() ||
                (email.isBlank() || !email.contains("@")) ||
                phoneNumber.isBlank() ||
                password.isBlank()

    }
}
