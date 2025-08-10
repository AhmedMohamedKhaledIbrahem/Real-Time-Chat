package com.example.realtimechat.features.chat.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.realtimechat.core.event.UiEvent
import com.example.realtimechat.features.chat.presentation.controller.add_request.AddRequestViewModel
import com.example.realtimechat.features.chat.presentation.controller.add_request.event.AddRequestEvent
import com.example.realtimechat.features.chat.presentation.controller.fcm.FcmViewModel
import com.example.realtimechat.features.chat.presentation.controller.fcm.event.FcmEvent
import com.example.realtimechat.ui.system_design.RealTimeChatButton
import com.example.realtimechat.ui.system_design.RealTimeChatTextField
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge

typealias receiverEmail = AddRequestEvent.SenderEmail
typealias receiverEmailFcm = FcmEvent.Email
typealias titleNotification = FcmEvent.Title
typealias bodyNotification = FcmEvent.Body

@Composable
fun HomeScreenRoot(
    snackbarHostState: SnackbarHostState,
    addRequestViewModel: AddRequestViewModel,
    fcmViewModel: FcmViewModel,
    signOutClick: () -> Unit
) {
    val addRequestState by addRequestViewModel.addRequestState.collectAsStateWithLifecycle()
    val fcmState by fcmViewModel.fcmState.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = 16.dp,
                vertical = 24.dp,
            )
            .padding(top = 48.dp)
    ) {

        val receiverEmail = addRequestState.receiverEmail ?: ""
        val senderEmail = addRequestState.senderEmail ?: ""

        AddRequestSection(
            modifier = Modifier.fillMaxWidth(),
            email = receiverEmail,
            onValueChange = {
                addRequestViewModel.onEvent(receiverEmail(it))
            },
            isLoading = fcmState.isLoading,
            onClick = {
                addRequestViewModel.onEvent(AddRequestEvent.AddRequestClicked)
                fcmViewModel.onEvent(receiverEmailFcm(receiverEmail))
                fcmViewModel.onEvent(titleNotification("New Request"))
                fcmViewModel.onEvent(bodyNotification("You have a new request from $senderEmail"))
                fcmViewModel.onEvent(FcmEvent.FcmClick)
            },
            signOutClick = signOutClick
        )
        val mergedEvents = merge(
            addRequestViewModel.addRequestEvent.map { "AddRequest" to it },
            fcmViewModel.fcmEvent.map { "Fcm" to it }
        )
        HomeEvent(
            event = mergedEvents,
            snackbarHostState = snackbarHostState
        )
    }


}

@Composable
fun HomeEvent(
    event: Flow<Pair<String, UiEvent>>,
    snackbarHostState: SnackbarHostState
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        event.collectLatest { (source, uiEvent) ->
            when (uiEvent) {
                is UiEvent.ShowSnackBar -> {
                    val message = uiEvent.message
                    snackbarHostState.showSnackbar(
                        "[${source}] ${message.asString(context)}"
                    )
                }

                else -> Unit
            }
        }
    }

}

@Composable
fun AddRequestSection(
    modifier: Modifier = Modifier,
    email: String,
    isLoading: Boolean,
    onValueChange: (String) -> Unit,
    onClick: () -> Unit,
    signOutClick: () -> Unit
) {
    RealTimeChatTextField(
        modifier = modifier,
        text = email,
        onValueChange = onValueChange,
        label = "Email",
        hint = "Enter your email",
    )
    Spacer(modifier = Modifier.height(16.dp))
    RealTimeChatButton(
        modifier = modifier,
        text = "Send Request",
        isLoading = isLoading,
        onClick = onClick
    )
    Spacer(modifier = Modifier.height(16.dp))
    Button(
        onClick = signOutClick
    ) { Text("sign out") }
}

