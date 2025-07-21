package com.example.realtimechatapp.features.authentication.presentation.controller.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realtimechatapp.core.event.UiEvent
import com.example.realtimechatapp.core.extension.asUiTextOrDefault
import com.example.realtimechatapp.core.extension.performUseCaseOperation
import com.example.realtimechatapp.core.ui_text.UiText
import com.example.realtimechatapp.features.authentication.domain.entity.SignUpEntity
import com.example.realtimechatapp.features.authentication.domain.usecase.signup.SignUpUseCase
import com.example.realtimechatapp.features.authentication.domain.validator.Validator
import com.example.realtimechatapp.features.authentication.presentation.controller.signup.event.SignUpEvent
import com.example.realtimechatapp.features.authentication.presentation.controller.signup.state.SignUpState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase,
    validator: Validator
) : ViewModel(), Validator by validator {
    private val _signUpEvent = Channel<UiEvent>()
    val signUpEvent = _signUpEvent.receiveAsFlow()
    var emailValidator by mutableStateOf<UiText?>(null)
        private set
    var nameValidator by mutableStateOf<UiText?>(null)
        private set
    var phoneValidator by mutableStateOf<UiText?>(null)
        private set
    var passwordValidator by mutableStateOf<UiText?>(null)
        private set

    private val _signUpState = MutableStateFlow(SignUpState())
    val signUpState = _signUpState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SignUpState()
    )

    fun onEvent(event: SignUpEvent) {
        when (event) {
            SignUpEvent.Click.SignIn -> {
                viewModelScope.launch {
                    _signUpEvent.send(UiEvent.NavEvent.SignInScreen)
                }
            }

            SignUpEvent.Click.SignUp -> {
                signUp()
            }

            is SignUpEvent.Input.Email -> {
                _signUpState.value = _signUpState.value.copy(email = event.email)
            }

            is SignUpEvent.Input.Name -> {
                _signUpState.value = _signUpState.value.copy(name = event.name)
            }

            is SignUpEvent.Input.Password -> {
                _signUpState.value = _signUpState.value.copy(password = event.password)
            }

            is SignUpEvent.Input.Phone -> {
                _signUpState.value = _signUpState.value.copy(phone = event.phone)
            }
        }
    }

    private fun signUp() = performUseCaseOperation(
        scope = viewModelScope,
        useCase = {
            _signUpState.update { it.copy(isLoading = true) }
            val name = _signUpState.value.name.orEmpty()
            val email = _signUpState.value.email.orEmpty()
            val phone = _signUpState.value.phone.orEmpty()
            val password = _signUpState.value.password.orEmpty()
            val signUpParams = SignUpEntity(name, email, phone, password)
            signUpUseCase.invoke(signUpParams = signUpParams)
        },
        onSuccess = {
            _signUpState.update { it.copy(isLoading = false) }
            _signUpEvent.send(UiEvent.NavEvent.SignInScreen)
        },
        onError = { error ->
            _signUpState.update { it.copy(isLoading = false) }
            _signUpEvent.send(UiEvent.ShowSnackBar(error.asUiTextOrDefault()))
        }
    )

    fun emailValidation() = performUseCaseOperation(
        scope = viewModelScope,
        useCase = {
            val email = _signUpState.value.email.orEmpty()
            isEmailValid(email = email)
        },
        onSuccess = {
            emailValidator = null
        },
        onError = {
            emailValidator = it.asUiTextOrDefault()
        },
    )

    fun nameValidation() = performUseCaseOperation(
        scope = viewModelScope,
        useCase = {
            val name = _signUpState.value.name.orEmpty()
            isNameValid(name = name)
        },
        onSuccess = {
            nameValidator = null
        },
        onError = { it.asUiTextOrDefault() }
    )

    fun phoneValidation() = performUseCaseOperation(
        scope = viewModelScope,
        useCase = {
            val phone = _signUpState.value.phone.orEmpty()
            isPhoneNumberValid(phoneNumber = phone)
        },
        onSuccess = {
            phoneValidator = null
        },
        onError = { it.asUiTextOrDefault() }
    )

    fun passwordValidation() = performUseCaseOperation(
        scope = viewModelScope,
        useCase = {
            val password = _signUpState.value.password.orEmpty()
            isPasswordValid(password = password)
        },
        onSuccess = {
            passwordValidator = null
        },
        onError = { it.asUiTextOrDefault() }
    )

}