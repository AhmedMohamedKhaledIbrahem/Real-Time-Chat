package com.example.realtimechat.features.authentication.domain.validator

import com.example.realtimechat.core.error.DomainError
import com.example.realtimechat.core.utils.Result

interface Validator {
    fun isEmailValid(email: String): Result<Unit, DomainError.Email>
    fun isPasswordValid(password: String): Result<Unit, DomainError.Password>
    fun isNameValid(name: String): Result<Unit, DomainError.Name>
    fun isPhoneNumberValid(phoneNumber: String): Result<Unit, DomainError.Phone>
}

class ValidatorImpl : Validator {
    override fun isEmailValid(email: String): Result<Unit, DomainError.Email> {
        val regex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
        if (!regex.matches(email)) {
            return Result.Error(DomainError.Email.INVALID_EMAIL)
        }
        return Result.Success(Unit)
    }

    override fun isPasswordValid(password: String): Result<Unit, DomainError.Password> {
        return when {
            password.length <= 6 -> {
                return Result.Error(DomainError.Password.TOO_SHORT)
            }
            !password.contains(Regex("[A-Z]")) -> {
                return Result.Error(DomainError.Password.NO_UPPERCASE)
            }
            !password.contains(Regex("[a-z]")) -> {
                return Result.Error(DomainError.Password.NO_LOWERCASE)
            }
            !password.contains(Regex("[0-9]")) -> {
                return Result.Error(DomainError.Password.NO_DIGIT)
            }
            !password.contains(Regex("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]")) -> {
                return Result.Error(DomainError.Password.NO_SPECIAL_CHARACTER)
            }
            else -> Result.Success(Unit)
        }


    }

    override fun isNameValid(name: String): Result<Unit, DomainError.Name> {
        val regex = Regex("^[A-Za-z][A-Za-z ]*$")
        if (!regex.matches(name)) {
            return Result.Error(DomainError.Name.INVALID_NAME)
        }
        return Result.Success(Unit)
    }

    override fun isPhoneNumberValid(phoneNumber: String): Result<Unit, DomainError.Phone> {
        val regex = Regex("^(010|011|012|015)[0-9]{8}$")
        if (!regex.matches(phoneNumber)) {
            return Result.Error(DomainError.Phone.INVALID_PHONE)
        }
        return Result.Success(Unit)

    }

}