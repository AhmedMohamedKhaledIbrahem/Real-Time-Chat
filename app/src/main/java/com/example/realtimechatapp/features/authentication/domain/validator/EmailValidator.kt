package com.example.realtimechatapp.features.authentication.domain.validator

interface EmailValidator {
    fun isValid(email: String): Boolean
}

class RegexEmailValidator : EmailValidator {
    override fun isValid(email: String): Boolean {
        val regex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
        return regex.matches(email)
    }

}