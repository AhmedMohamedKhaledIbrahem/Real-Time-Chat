package com.example.realtimechat.features.chat.presentation.controller.fcm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realtimechat.R
import com.example.realtimechat.core.event.UiEvent
import com.example.realtimechat.core.extension.asUiTextOrDefault
import com.example.realtimechat.core.extension.performUseCaseOperation
import com.example.realtimechat.core.shared_preference.RealTimeChatSharedPreference
import com.example.realtimechat.core.ui_text.UiText
import com.example.realtimechat.features.chat.data.model.SendFCMRequestModel
import com.example.realtimechat.features.chat.domain.usecase.get_fcm_token_by_email.GetFcmTokenByEmailUseCase
import com.example.realtimechat.features.chat.domain.usecase.send_fcm_request.SendFcmRequestUseCase
import com.example.realtimechat.features.chat.presentation.controller.fcm.event.FcmEvent
import com.example.realtimechat.features.chat.presentation.controller.fcm.state.FcmState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FcmViewModel(
    private val getFcmTokenByEmailUseCase: GetFcmTokenByEmailUseCase,
    private val sendFcmRequestUseCase: SendFcmRequestUseCase,
    private val sharedPreference: RealTimeChatSharedPreference
) : ViewModel() {
    private val _fcmEvent = Channel<UiEvent>()
    val fcmEvent = _fcmEvent.receiveAsFlow()

    private val _fcmState = MutableStateFlow(FcmState())
    val fcmState = _fcmState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = FcmState(),
    )


    fun onEvent(event: FcmEvent) {
        when (event) {
            is FcmEvent.FcmClick -> {
                getFcmToken()
                sendFcmRequest()
            }

            is FcmEvent.Body -> {
                _fcmState.update { it.copy(body = event.body) }
            }

            is FcmEvent.Title -> {
                _fcmState.update { it.copy(title = event.title) }
            }

            is FcmEvent.Email -> {
                _fcmState.update { it.copy(email = event.email) }
            }
        }
    }

    private fun getFcmToken() {
        val email = fcmState.value.email
        if (email != null) {
            performUseCaseOperation(
                scope = viewModelScope,
                useCase = {
                    getFcmTokenByEmailUseCase(email)
                },
                onSuccess = {
                    Unit
                },
                onError = {
                    _fcmEvent.send(
                        UiEvent.ShowSnackBar(
                            it.asUiTextOrDefault()
                        )
                    )
                }
            )
        }
    }

    private fun sendFcmRequest() {
        val fcmToken = sharedPreference.getReceiverToken()
        val title = fcmState.value.title
        val body = fcmState.value.body
        val channelUid = sharedPreference.getChannelUid()
        if (fcmToken == null || title == null || body == null || channelUid == null) {
            viewModelScope.launch {
                _fcmEvent.send(
                    UiEvent.ShowSnackBar(
                        UiText.from(R.string.error_network_unknown)
                    )
                )
            }
            return
        }
        performUseCaseOperation(
            scope = viewModelScope,
            useCase = {
                _fcmState.update { it.copy(isLoading = true) }

                val sendFCMRequestModel = SendFCMRequestModel(
                    fcmToken = fcmToken,
                    title = title,
                    body = body,
                    channelId = channelUid
                )
                sendFcmRequestUseCase(sendFcmRequestParams = sendFCMRequestModel)
            },
            onSuccess = {
                _fcmState.update { it.copy(isLoading = false) }

            },
            onError = {
                _fcmState.update { state -> state.copy(isLoading = false) }
                _fcmEvent.send(
                    UiEvent.ShowSnackBar(
                        it.asUiTextOrDefault()
                    )
                )
            },
        )
    }
}