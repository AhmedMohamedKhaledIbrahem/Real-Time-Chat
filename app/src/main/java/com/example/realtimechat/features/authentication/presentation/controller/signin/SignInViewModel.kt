package com.example.realtimechat.features.authentication.presentation.controller.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realtimechat.core.event.UiEvent
import com.example.realtimechat.core.extension.asUiTextOrDefault
import com.example.realtimechat.core.extension.performUseCaseOperation
import com.example.realtimechat.features.authentication.domain.usecase.signin.SignInUseCase
import com.example.realtimechat.features.authentication.presentation.controller.signin.event.SignInEvent
import com.example.realtimechat.features.authentication.presentation.controller.signin.state.SignInState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignInViewModel(private val signInUseCase: SignInUseCase) : ViewModel() {
    private val _signInEvent = Channel<UiEvent>()
    val signInEvent = _signInEvent.receiveAsFlow()
    private val _signInState = MutableStateFlow(SignInState())
    val signInState = _signInState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = SignInState()
    )


    fun onEvent(event: SignInEvent) {
        when (event) {
            is SignInEvent.Click.ForgetPassword -> {
                viewModelScope.launch {
                    _signInEvent.send(UiEvent.NavEvent.ForgetPasswordScreen)
                }
            }

            is SignInEvent.Click.SignIn -> {
                signIn()
            }

            is SignInEvent.Click.SignUp -> {
                viewModelScope.launch {
                    _signInEvent.send(UiEvent.NavEvent.SignUpScreen)
                }
            }

            is SignInEvent.Email -> {
                _signInState.update { it.copy(user = event.email) }
            }

            is SignInEvent.Password -> {
                _signInState.update { it.copy(password = event.password) }
            }
        }
    }

    private fun signIn() = performUseCaseOperation(
        scope = viewModelScope,
        useCase = {
            _signInState.update { it.copy(isLoading = true) }
            signInUseCase.invoke(
                email = signInState.value.user.orEmpty(),
                password = signInState.value.password.orEmpty(),
            )
        },
        onSuccess = {
            _signInState.update { it.copy(isLoading = false) }
            _signInEvent.send(UiEvent.NavEvent.HomeScreen)

        },
        onError = { error ->
            _signInState.update { it.copy(isLoading = false) }
            _signInEvent.send(UiEvent.ShowSnackBar(error.asUiTextOrDefault()))
        },
    )

}
