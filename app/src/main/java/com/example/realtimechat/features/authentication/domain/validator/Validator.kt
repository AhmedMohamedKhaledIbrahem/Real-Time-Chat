package com.example.realtimechat.features.authentication.domain.validator

import com.example.realtimechat.core.error.AuthDomainError
import com.example.realtimechat.core.utils.Result

interface Validator {
    fun isEmailValid(email: String): Result<Unit, AuthDomainError.Email>
    fun isPasswordValid(password: String): Result<Unit, AuthDomainError.Password>
    fun isNameValid(name: String): Result<Unit, AuthDomainError.Name>
    fun isPhoneNumberValid(phoneNumber: String): Result<Unit, AuthDomainError.Phone>
}

class ValidatorImpl : Validator {
    override fun isEmailValid(email: String): Result<Unit, AuthDomainError.Email> {
        val regex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
        if (!regex.matches(email)) {
            return Result.Error(AuthDomainError.Email.INVALID_EMAIL)
        }
        return Result.Success(Unit)
    }

    override fun isPasswordValid(password: String): Result<Unit, AuthDomainError.Password> {
        return when {
            password.length <= 6 -> {
                return Result.Error(AuthDomainError.Password.TOO_SHORT)
            }
            !password.contains(Regex("[A-Z]")) -> {
                return Result.Error(AuthDomainError.Password.NO_UPPERCASE)
            }
            !password.contains(Regex("[a-z]")) -> {
                return Result.Error(AuthDomainError.Password.NO_LOWERCASE)
            }
            !password.contains(Regex("[0-9]")) -> {
                return Result.Error(AuthDomainError.Password.NO_DIGIT)
            }
            !password.contains(Regex("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]")) -> {
                return Result.Error(AuthDomainError.Password.NO_SPECIAL_CHARACTER)
            }
            else -> Result.Success(Unit)
        }


    }

    override fun isNameValid(name: String): Result<Unit, AuthDomainError.Name> {
        val regex = Regex("^[A-Za-z][A-Za-z ]*$")
        if (!regex.matches(name)) {
            return Result.Error(AuthDomainError.Name.INVALID_NAME)
        }
        return Result.Success(Unit)
    }

    override fun isPhoneNumberValid(phoneNumber: String): Result<Unit, AuthDomainError.Phone> {
        val regex = Regex("^(010|011|012|015)[0-9]{8}$")
        if (!regex.matches(phoneNumber)) {
            return Result.Error(AuthDomainError.Phone.INVALID_PHONE)
        }
        return Result.Success(Unit)

    }

}