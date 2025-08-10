package com.example.realtimechat.features.chat.presentation.controller.add_request

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realtimechat.R
import com.example.realtimechat.core.error.DomainError
import com.example.realtimechat.core.event.UiEvent
import com.example.realtimechat.core.extension.asUiTextOrDefault
import com.example.realtimechat.core.extension.performUseCaseOperation
import com.example.realtimechat.core.shared_preference.RealTimeChatSharedPreference
import com.example.realtimechat.core.ui_text.UiText
import com.example.realtimechat.core.utils.Result
import com.example.realtimechat.features.chat.data.model.AddRequestModel
import com.example.realtimechat.features.chat.domain.usecase.add_request.AddRequestUseCase
import com.example.realtimechat.features.chat.presentation.controller.add_request.event.AddRequestEvent
import com.example.realtimechat.features.chat.presentation.controller.add_request.state.AddRequestState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class AddRequestViewModel(
    private val addRequestUseCase: AddRequestUseCase,
    private val sharedPreference: RealTimeChatSharedPreference
) : ViewModel() {
    private val _addRequestEvent = Channel<UiEvent>()
    val addRequestEvent = _addRequestEvent.receiveAsFlow()
    private val _addRequestState = MutableStateFlow(AddRequestState())
    val addRequestState = _addRequestState.stateIn(
        scope = viewModelScope,
        initialValue = AddRequestState(),
        started = SharingStarted.WhileSubscribed(5000L)
    )

    fun onEvent(event: AddRequestEvent) {
        when (event) {
            AddRequestEvent.AddRequestClicked -> addRequest()

            is AddRequestEvent.SenderEmail -> {
                _addRequestState.update {
                    it.copy(receiverEmail = event.email)
                }
            }

        }
    }

    private fun addRequest() = performUseCaseOperation(
        scope = viewModelScope,
        useCase = {
            _addRequestState.update { it.copy(isLoading = true) }
            val senderEmail = sharedPreference.getEmail()
            val receiverEmail = _addRequestState.value.receiverEmail
            if (senderEmail == null || receiverEmail == null) {
                return@performUseCaseOperation Result.Error(DomainError.Network.UNKNOWN)
            }
            _addRequestState.update { it.copy(senderEmail = senderEmail) }
            val requestModel = AddRequestModel(
                senderEmail = senderEmail,
                receiverEmail = receiverEmail,
                timestamp = System.currentTimeMillis()
            )
            addRequestUseCase.invoke(request = requestModel)
        },
        onSuccess = {
            _addRequestEvent.send(
                UiEvent.ShowSnackBar(
                    UiText.from(
                        R.string.request_added_successfully
                    )
                )
            )

        },
        onError = {
            _addRequestEvent.send(UiEvent.ShowSnackBar(it.asUiTextOrDefault()))
        },
    )
}