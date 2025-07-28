package com.example.realtimechat.features.authentication.presentation.controller.forgetpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realtimechat.R
import com.example.realtimechat.core.event.UiEvent
import com.example.realtimechat.core.extension.asUiTextOrDefault
import com.example.realtimechat.core.extension.performUseCaseOperation
import com.example.realtimechat.core.ui_text.UiText
import com.example.realtimechat.features.authentication.domain.usecase.forgetpassword.ForgetPasswordUseCase
import com.example.realtimechat.features.authentication.presentation.controller.forgetpassword.event.ForgetPasswordEvent
import com.example.realtimechat.features.authentication.presentation.controller.forgetpassword.state.ForgetPasswordState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class ForgetPasswordViewModel(
    private val forgetPasswordUseCase: ForgetPasswordUseCase
) : ViewModel() {
    private val _forgetPasswordEvent = Channel<UiEvent>()
    val forgetPasswordEvent = _forgetPasswordEvent.receiveAsFlow()
    private val _forgetPasswordState = MutableStateFlow(ForgetPasswordState())
    val forgetPasswordState = _forgetPasswordState.stateIn(
        scope = viewModelScope,
        initialValue = ForgetPasswordState(),
        started = SharingStarted.WhileSubscribed(5000),
    )

    fun onEvent(event: ForgetPasswordEvent) {
        when (event) {
            is ForgetPasswordEvent.EmailInput -> {
                _forgetPasswordState.update { it.copy(email = event.email) }
            }

            ForgetPasswordEvent.ForgetPasswordClick -> {
                forgetPassword()
            }
        }
    }

    private fun forgetPassword() = performUseCaseOperation(
        scope = viewModelScope,
        useCase = {
            _forgetPasswordState.update { it.copy(isLoading = true) }
            val email = _forgetPasswordState.value.email.orEmpty()
            forgetPasswordUseCase.invoke(email = email)
        },
        onSuccess = {
            _forgetPasswordState.update { it.copy(isLoading = false) }
            _forgetPasswordEvent.send(
                UiEvent.CombinedEvents(
                    listOf(
                        UiEvent.ShowSnackBar(
                            UiText.from(resId = R.string.forgot_password_link_sent_message)
                        ),
                        UiEvent.NavEvent.SignInScreen
                    )
                )
            )
        },
        onError = { error ->
            _forgetPasswordState.update { it.copy(isLoading = false) }
            _forgetPasswordEvent.send(
                UiEvent.ShowSnackBar(
                    error.asUiTextOrDefault()
                )
            )
        }
    )
}