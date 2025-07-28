package com.example.realtimechat.features.authentication.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsIgnoringVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.realtimechat.R
import com.example.realtimechat.core.event.UiEvent
import com.example.realtimechat.core.event.combinedEvent
import com.example.realtimechat.core.utils.DeviceConfiguration
import com.example.realtimechat.features.authentication.presentation.controller.forgetpassword.ForgetPasswordViewModel
import com.example.realtimechat.features.authentication.presentation.controller.forgetpassword.event.ForgetPasswordEvent
import com.example.realtimechat.ui.system_design.RealTimeChatButton
import com.example.realtimechat.ui.system_design.RealTimeChatTextField
import kotlinx.coroutines.flow.Flow

typealias forgetPasswordEmailInput = ForgetPasswordEvent.EmailInput
typealias forgetPasswordOnSubmitClick = ForgetPasswordEvent.ForgetPasswordClick

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ForgetPasswordScreenRoot(
    forgetPasswordViewModel: ForgetPasswordViewModel,
    snackbarHostState: SnackbarHostState,
    onForgetPasswordClick: () -> Unit,
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClasses(windowSizeClass)
    val rootModifier = Modifier
        .fillMaxSize()
        .padding(
            horizontal = 16.dp,
            vertical = 24.dp,
        )
        .padding(top = 48.dp)
    when (deviceConfiguration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> {
            Column(
                modifier = rootModifier
                    .verticalScroll(rememberScrollState())
                    .windowInsetsPadding(WindowInsets.navigationBarsIgnoringVisibility),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ForgetPasswordHeaderSection(
                    modifier = Modifier.fillMaxWidth(),
                )
                val forgotPasswordState by forgetPasswordViewModel.forgetPasswordState.collectAsStateWithLifecycle()
                ForgetPasswordFormSection(
                    modifier = Modifier.fillMaxWidth(),
                    emailText = forgotPasswordState.email.orEmpty(),
                    isLoading = forgotPasswordState.isLoading,
                    onEmailTextChange = { forgetPasswordViewModel.onEvent(forgetPasswordEmailInput(it)) },
                    onSubmitClick = { forgetPasswordViewModel.onEvent(forgetPasswordOnSubmitClick) }
                )
            }
        }

        DeviceConfiguration.MOBILE_LANDSCAPE -> {
            Row(
                modifier = rootModifier
                    .verticalScroll(rememberScrollState())
                    .windowInsetsPadding(WindowInsets.navigationBarsIgnoringVisibility)
                    .windowInsetsPadding(WindowInsets.displayCutout),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ForgetPasswordHeaderSection(
                    modifier = Modifier.weight(1f),
                )
                val forgotPasswordState by forgetPasswordViewModel.forgetPasswordState.collectAsStateWithLifecycle()
                ForgetPasswordFormSection(
                    modifier = Modifier.weight(1f),
                    emailText = forgotPasswordState.email.orEmpty(),
                    isLoading = forgotPasswordState.isLoading,
                    onEmailTextChange = { forgetPasswordViewModel.onEvent(forgetPasswordEmailInput(it)) },
                    onSubmitClick = { forgetPasswordViewModel.onEvent(forgetPasswordOnSubmitClick) }
                )
            }
        }

        DeviceConfiguration.TABLET_PORTRAIT,
        DeviceConfiguration.TABLET_LANDSCAPE,
        DeviceConfiguration.DESKTOP -> {
            Column(
                modifier = rootModifier
                    .verticalScroll(rememberScrollState())
                    .windowInsetsPadding(WindowInsets.navigationBarsIgnoringVisibility),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ForgetPasswordHeaderSection(
                    modifier = Modifier.widthIn(max = 540.dp),
                    alignment = Alignment.CenterHorizontally
                )
                val forgotPasswordState by forgetPasswordViewModel.forgetPasswordState.collectAsStateWithLifecycle()
                ForgetPasswordFormSection(
                    modifier = Modifier.widthIn(max = 540.dp),
                    emailText = forgotPasswordState.email.orEmpty(),
                    isLoading = forgotPasswordState.isLoading,
                    onEmailTextChange = { forgetPasswordViewModel.onEvent(forgetPasswordEmailInput(it)) },
                    onSubmitClick = { forgetPasswordViewModel.onEvent(forgetPasswordOnSubmitClick) }
                )
            }
        }
    }
    ForgetPasswordEvent(
        uiEvent = forgetPasswordViewModel.forgetPasswordEvent,
        snackbarHostState = snackbarHostState,
        onForgetPasswordClick = onForgetPasswordClick
    )
}

@Composable
private fun ForgetPasswordHeaderSection(
    modifier: Modifier = Modifier,
    alignment: Alignment.Horizontal = Alignment.Start,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = alignment
    ) {
        Text(
            stringResource(R.string.forgetpassword),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.height(8.dp))
        Text(
            stringResource(R.string.to_reset_your_password_please_enter_the_email_address_associated_with_your_account),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun ForgetPasswordFormSection(
    modifier: Modifier = Modifier,
    emailText: String,
    isLoading: Boolean,
    onEmailTextChange: (String) -> Unit,
    onSubmitClick: () -> Unit,
) {
    Column(modifier = modifier) {
        RealTimeChatTextField(
            modifier = Modifier.fillMaxWidth(),
            text = emailText,
            onValueChange = onEmailTextChange,
            label = stringResource(R.string.email),
            hint = "join@example.com",
        )
        Spacer(Modifier.height(16.dp))
        RealTimeChatButton(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            text = stringResource(R.string.submit),
            isLoading = isLoading,
            enabled = !isLoading,
            onClick = onSubmitClick
        )
    }

}

@Composable
private fun ForgetPasswordEvent(
    uiEvent: Flow<UiEvent>,
    snackbarHostState: SnackbarHostState,
    onForgetPasswordClick: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(uiEvent) {
        uiEvent.collect { event ->
            when (event) {
                is UiEvent.NavEvent.SignInScreen -> onForgetPasswordClick()

                is UiEvent.ShowSnackBar -> {
                    val message = event.message.asString(context)
                    snackbarHostState.showSnackbar(message)
                }

                is UiEvent.CombinedEvents -> {
                    combinedEvent(
                        event = event.event,
                        onShowMessage = { message ->
                            val message = message.asString(context)
                            snackbarHostState.showSnackbar(message)
                        },
                        onNavigate = { navEvent ->
                            when (navEvent) {
                                is UiEvent.NavEvent.SignInScreen -> onForgetPasswordClick()
                                else -> Unit
                            }
                        }
                    )

                }

                else -> Unit
            }

        }
    }
}