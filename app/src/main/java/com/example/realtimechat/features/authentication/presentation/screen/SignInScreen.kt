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
import com.example.realtimechat.core.utils.DeviceConfiguration
import com.example.realtimechat.features.authentication.presentation.controller.signin.SignInViewModel
import com.example.realtimechat.features.authentication.presentation.controller.signin.event.SignInEvent
import com.example.realtimechat.ui.system_design.RealTimeChatButton
import com.example.realtimechat.ui.system_design.RealTimeChatLink
import com.example.realtimechat.ui.system_design.RealTimeChatTextField
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SignInScreenRoot(
    signInViewModel: SignInViewModel,
    snackBarHostState: SnackbarHostState,
    onForgetPasswordClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onHomeScreenClick: () -> Unit,
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
                modifier = rootModifier,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                SignInHeaderSection(
                    modifier = Modifier.fillMaxWidth()
                )
                val signInState by signInViewModel.signInState.collectAsStateWithLifecycle()
                SignInFormSection(
                    emailText = signInState.user.orEmpty(),
                    passwordText = signInState.password.orEmpty(),
                    onEmailChange = {
                        signInViewModel.onEvent(SignInEvent.Email(it))
                    },
                    onPasswordChange = {
                        signInViewModel.onEvent(SignInEvent.Password(it))
                    },
                    isLoading = signInState.isLoading,
                    onSignInClick = {
                        signInViewModel.onEvent(SignInEvent.Click.SignIn)
                    },
                    onSignUpClick = {
                        signInViewModel.onEvent(SignInEvent.Click.SignUp)
                    },
                    onForgetPasswordClick = {
                        signInViewModel.onEvent(SignInEvent.Click.ForgetPassword)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                SignInEvent(
                    uiEvent = signInViewModel.signInEvent,
                    snackBarHostState = snackBarHostState,
                    onForgetPasswordClick = onForgetPasswordClick,
                    onSignUpClick = onSignUpClick,
                    onHomeScreenClick = onHomeScreenClick
                )
            }
        }

        DeviceConfiguration.MOBILE_LANDSCAPE -> {
            Row(
                modifier = rootModifier
                    .windowInsetsPadding(WindowInsets.displayCutout)
                    .windowInsetsPadding(WindowInsets.navigationBarsIgnoringVisibility),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                SignInHeaderSection(
                    modifier = Modifier.weight(1f)
                )
                val signInState by signInViewModel.signInState.collectAsStateWithLifecycle()
                SignInFormSection(
                    emailText = signInState.user.orEmpty(),
                    passwordText = signInState.password.orEmpty(),
                    onEmailChange = {
                        signInViewModel.onEvent(SignInEvent.Email(it))
                    },
                    onPasswordChange = {
                        signInViewModel.onEvent(SignInEvent.Password(it))
                    },
                    isLoading = signInState.isLoading,
                    onSignInClick = {
                        signInViewModel.onEvent(SignInEvent.Click.SignIn)
                    },
                    onSignUpClick = {
                        signInViewModel.onEvent(SignInEvent.Click.SignUp)
                    },
                    onForgetPasswordClick = {
                        signInViewModel.onEvent(SignInEvent.Click.ForgetPassword)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                )

                SignInEvent(
                    uiEvent = signInViewModel.signInEvent,
                    snackBarHostState = snackBarHostState,
                    onForgetPasswordClick = onForgetPasswordClick,
                    onSignUpClick = onSignUpClick,
                    onHomeScreenClick = onHomeScreenClick
                )
            }
        }

        DeviceConfiguration.TABLET_PORTRAIT,
        DeviceConfiguration.TABLET_LANDSCAPE,
        DeviceConfiguration.DESKTOP -> {
            Column(
                modifier = rootModifier
                    .verticalScroll(rememberScrollState())
                    .padding(top = 48.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                SignInHeaderSection(
                    modifier = Modifier
                        .widthIn(max = 540.dp),
                    alignment = Alignment.CenterHorizontally
                )
                val signInState by signInViewModel.signInState.collectAsStateWithLifecycle()
                SignInFormSection(
                    emailText = signInState.user.orEmpty(),
                    passwordText = signInState.password.orEmpty(),
                    onEmailChange = {
                        signInViewModel.onEvent(SignInEvent.Email(it))
                    },
                    onPasswordChange = {
                        signInViewModel.onEvent(SignInEvent.Password(it))
                    },
                    isLoading = signInState.isLoading,
                    onSignInClick = {
                        signInViewModel.onEvent(SignInEvent.Click.SignIn)
                    },
                    onSignUpClick = {
                        signInViewModel.onEvent(SignInEvent.Click.SignUp)
                    },
                    onForgetPasswordClick = {
                        signInViewModel.onEvent(SignInEvent.Click.ForgetPassword)
                    },
                    modifier = Modifier.widthIn(max = 540.dp)
                )
                SignInEvent(
                    uiEvent = signInViewModel.signInEvent,
                    snackBarHostState = snackBarHostState,
                    onForgetPasswordClick = onForgetPasswordClick,
                    onSignUpClick = onSignUpClick,
                    onHomeScreenClick = onHomeScreenClick
                )
            }
        }
    }
}


@Composable
private fun SignInHeaderSection(
    modifier: Modifier = Modifier,
    alignment: Alignment.Horizontal = Alignment.Start,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = alignment
    ) {
        Text(
            text = stringResource(R.string.sign_in),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.capture_your_thoughts_and_ideas),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun SignInFormSection(
    emailText: String,
    passwordText: String,
    isLoading: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignInClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onForgetPasswordClick: () -> Unit,
    modifier: Modifier
) {
    Column(modifier = modifier) {
        RealTimeChatTextField(
            text = emailText,
            onValueChange = onEmailChange,
            label = stringResource(R.string.email),
            hint = "join@example.com",
            isInputSecret = false,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        RealTimeChatTextField(
            text = passwordText,
            onValueChange = onPasswordChange,
            label = stringResource(R.string.password),
            hint = "Password",
            isInputSecret = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        RealTimeChatLink(
            text = stringResource(R.string.forget_password),
            onClick = onForgetPasswordClick,
            modifier = Modifier.align(Alignment.End)
        )
        Spacer(modifier = Modifier.height(16.dp))
        RealTimeChatButton(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            text = "Sign In",
            isLoading = isLoading,
            enabled = !isLoading,
            onClick = onSignInClick
        )
        Spacer(modifier = Modifier.height(16.dp))
        RealTimeChatLink(
            text = stringResource(R.string.don_t_have_an_account_sign_up),
            onClick = onSignUpClick,
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
        )


    }
}


@Composable
private fun SignInEvent(
    uiEvent: Flow<UiEvent>,
    snackBarHostState: SnackbarHostState,
    onForgetPasswordClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onHomeScreenClick: () -> Unit,
) {
    val context = LocalContext.current

    LaunchedEffect(uiEvent) {
        uiEvent.collectLatest { event ->
            when (event) {
                is UiEvent.NavEvent.ForgetPasswordScreen -> {
                    onForgetPasswordClick()
                }

                is UiEvent.NavEvent.HomeScreen -> {
                    onHomeScreenClick()
                }

                is UiEvent.NavEvent.SignUpScreen -> {
                    onSignUpClick()
                }

                is UiEvent.ShowSnackBar -> {
                    val message = event.message.asString(context = context)
                    snackBarHostState.showSnackbar(message = message)
                }

                else -> Unit
            }
        }
    }
}