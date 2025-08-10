package com.example.realtimechat.features.authentication.presentation.controller.is_logged

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realtimechat.core.event.UiEvent
import com.example.realtimechat.core.extension.performUseCaseOperation
import com.example.realtimechat.features.authentication.domain.usecase.is_logged.IsLoggedUseCase
import com.example.realtimechat.core.ui_text.asUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class IsLoggedViewModel(private val isLoggedUseCase: IsLoggedUseCase) : ViewModel() {
    private val _isLoggedEvent = Channel<UiEvent>()
    val isLoggedEvent = _isLoggedEvent.receiveAsFlow()

    fun isLogged() = performUseCaseOperation(
        scope = viewModelScope,
        useCase = {
            isLoggedUseCase.invoke()
        },
        onSuccess = { result ->
            when (result) {
                true -> {
                    _isLoggedEvent.send(UiEvent.NavEvent.HomeScreen)
                }

                false -> {
                    _isLoggedEvent.send(UiEvent.NavEvent.SignInScreen)
                }
            }
        },
        onError = { resultError ->
            _isLoggedEvent.send(UiEvent.ShowSnackBar(resultError.error.asUiText()))
        }
    )
}