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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.realtimechat.R
import com.example.realtimechat.core.event.UiEvent
import com.example.realtimechat.core.event.combinedEvent
import com.example.realtimechat.core.ui_text.UiText
import com.example.realtimechat.core.utils.DeviceConfiguration
import com.example.realtimechat.features.authentication.presentation.controller.signup.SignUpViewModel
import com.example.realtimechat.features.authentication.presentation.controller.signup.event.SignUpEvent
import com.example.realtimechat.ui.system_design.RealTimeChatButton
import com.example.realtimechat.ui.system_design.RealTimeChatLink
import com.example.realtimechat.ui.system_design.RealTimeChatTextField
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

typealias name = SignUpEvent.Input.Name
typealias email = SignUpEvent.Input.Email
typealias phone = SignUpEvent.Input.Phone
typealias password = SignUpEvent.Input.Password
typealias onEventSignUpClick = SignUpEvent.Click.SignUp
typealias onEventSignInClick = SignUpEvent.Click.SignIn

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SignUpScreenRoot(
    signUpViewModel: SignUpViewModel,
    snackBarHostState: SnackbarHostState,
    onSignUpClick: () -> Unit,
    onSignInClick: () -> Unit,

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
                SignUpHeaderSection(
                    modifier = Modifier.fillMaxWidth(),
                )
                val signUpState by signUpViewModel.signUpState.collectAsStateWithLifecycle()
                val nameValidator = signUpViewModel.nameValidator
                val emailValidator = signUpViewModel.emailValidator
                val phoneValidator = signUpViewModel.phoneValidator
                val passwordValidator = signUpViewModel.passwordValidator
                SignUpFormSection(
                    modifier = Modifier.fillMaxWidth(),
                    nameText = signUpState.name.orEmpty(),
                    emailText = signUpState.email.orEmpty(),
                    phoneText = signUpState.phone.orEmpty(),
                    passwordText = signUpState.password.orEmpty(),
                    onNameChange = {
                        signUpViewModel.onEvent(name(it))
                        signUpViewModel.nameValidation()
                    },
                    onEmailChange = {
                        signUpViewModel.onEvent(email(it))
                        signUpViewModel.emailValidation()
                    },
                    onPhoneChange = {
                        signUpViewModel.onEvent(phone(it))
                        signUpViewModel.phoneValidation()
                    },
                    onPasswordChange = {
                        signUpViewModel.onEvent(password(it))
                        signUpViewModel.passwordValidation()
                    },
                    onSignUpClick = {
                        signUpViewModel.onEvent(onEventSignUpClick)
                    },
                    onSignInClick = {
                        signUpViewModel.onEvent(onEventSignInClick)
                    },
                    nameError = nameValidator,
                    emailError = emailValidator,
                    phoneError = phoneValidator,
                    passwordError = passwordValidator,
                    isLoading = signUpState.isLoading,
                )
            }
        }

        DeviceConfiguration.MOBILE_LANDSCAPE -> {
            Row(
                modifier = rootModifier
                    .verticalScroll(rememberScrollState())
                    .windowInsetsPadding(WindowInsets.displayCutout)
                    .windowInsetsPadding(WindowInsets.navigationBarsIgnoringVisibility),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SignUpHeaderSection(
                    modifier = Modifier.weight(1f),
                )
                val signUpState by signUpViewModel.signUpState.collectAsStateWithLifecycle()
                val nameValidator = signUpViewModel.nameValidator
                val emailValidator = signUpViewModel.emailValidator
                val phoneValidator = signUpViewModel.phoneValidator
                val passwordValidator = signUpViewModel.passwordValidator
                SignUpFormSection(
                    modifier = Modifier.weight(1f),
                    nameText = signUpState.name.orEmpty(),
                    emailText = signUpState.email.orEmpty(),
                    phoneText = signUpState.phone.orEmpty(),
                    passwordText = signUpState.password.orEmpty(),
                    onNameChange = {
                        signUpViewModel.onEvent(name(it))
                        signUpViewModel.nameValidation()
                    },
                    onEmailChange = {
                        signUpViewModel.onEvent(email(it))
                        signUpViewModel.emailValidation()
                    },
                    onPhoneChange = {
                        signUpViewModel.onEvent(phone(it))
                        signUpViewModel.phoneValidation()
                    },
                    onPasswordChange = {
                        signUpViewModel.onEvent(password(it))
                        signUpViewModel.passwordValidation()
                    },
                    onSignUpClick = {
                        signUpViewModel.onEvent(onEventSignUpClick)
                    },
                    onSignInClick = {
                        signUpViewModel.onEvent(onEventSignInClick)
                    },
                    nameError = nameValidator,
                    emailError = emailValidator,
                    phoneError = phoneValidator,
                    passwordError = passwordValidator,
                    isLoading = signUpState.isLoading,
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
                SignUpHeaderSection(
                    modifier = Modifier
                        .widthIn(max = 540.dp),
                    alignment = Alignment.CenterHorizontally
                )
                val signUpState by signUpViewModel.signUpState.collectAsStateWithLifecycle()
                val nameValidator = signUpViewModel.nameValidator
                val emailValidator = signUpViewModel.emailValidator
                val phoneValidator = signUpViewModel.phoneValidator
                val passwordValidator = signUpViewModel.passwordValidator
                SignUpFormSection(
                    modifier = Modifier.widthIn(max = 540.dp),
                    nameText = signUpState.name.orEmpty(),
                    emailText = signUpState.email.orEmpty(),
                    phoneText = signUpState.phone.orEmpty(),
                    passwordText = signUpState.password.orEmpty(),
                    onNameChange = {
                        signUpViewModel.onEvent(name(it))
                        signUpViewModel.nameValidation()
                    },
                    onEmailChange = {
                        signUpViewModel.onEvent(email(it))
                        signUpViewModel.emailValidation()
                    },
                    onPhoneChange = {
                        signUpViewModel.onEvent(phone(it))
                        signUpViewModel.phoneValidation()
                    },
                    onPasswordChange = {
                        signUpViewModel.onEvent(password(it))
                        signUpViewModel.passwordValidation()
                    },
                    onSignUpClick = {
                        signUpViewModel.onEvent(onEventSignUpClick)
                    },
                    onSignInClick = {
                        signUpViewModel.onEvent(onEventSignInClick)
                    },
                    nameError = nameValidator,
                    emailError = emailValidator,
                    phoneError = phoneValidator,
                    passwordError = passwordValidator,
                    isLoading = signUpState.isLoading,
                )
            }

        }

    }

    SignUpEvent(
        uiEvent = signUpViewModel.signUpEvent,
        snackBarHostState = snackBarHostState,
        onSignUpClick = onSignUpClick,
        onSignInClick = onSignInClick,
    )
}

@Composable
private fun SignUpHeaderSection(
    modifier: Modifier = Modifier,
    alignment: Alignment.Horizontal = Alignment.Start,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = alignment
    ) {
        Text(
            stringResource(R.string.sign_up),
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            stringResource(R.string.create_an_account_to_start_chatting),
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun SignUpFormSection(
    modifier: Modifier = Modifier,
    nameText: String,
    emailText: String,
    phoneText: String,
    passwordText: String,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    nameError: UiText?,
    emailError: UiText?,
    phoneError: UiText?,
    passwordError: UiText?,
    isLoading: Boolean,
    onSignUpClick: () -> Unit,
    onSignInClick: () -> Unit,
) {
    Column(modifier = modifier) {
        RealTimeChatTextField(
            modifier = Modifier.fillMaxWidth(),
            text = nameText,
            onValueChange = onNameChange,
            label = stringResource(R.string.name),
            hint = "john",
            isInputSecret = false,
            isError = nameError != null,
            supportingText = {
                nameError?.let { Text(it.asString()) }
            }
        )
        Spacer(Modifier.height(16.dp))
        RealTimeChatTextField(
            modifier = Modifier.fillMaxWidth(),
            text = emailText,
            onValueChange = onEmailChange,
            label = stringResource(R.string.email),
            hint = "join@example.com",
            isInputSecret = false,
            isError = emailError != null,
            supportingText = {
                emailError?.let { Text(it.asString()) }
            }
        )
        Spacer(Modifier.height(16.dp))
        RealTimeChatTextField(
            modifier = Modifier.fillMaxWidth(),
            text = phoneText,
            onValueChange = onPhoneChange,
            label = stringResource(R.string.phone),
            hint = "010-1234-5678",
            isError = phoneError != null,
            supportingText = {
                phoneError?.let { Text(it.asString()) }
            }
        )
        Spacer(Modifier.height(16.dp))
        RealTimeChatTextField(
            modifier = Modifier.fillMaxWidth(),
            text = passwordText,
            onValueChange = onPasswordChange,
            label = stringResource(R.string.password),
            hint = "Password",
            isInputSecret = true,
            isError = passwordError != null,
            supportingText = {
                passwordError?.let {
                    Text(it.asString())
                }
            }
        )
        Spacer(Modifier.height(16.dp))
        RealTimeChatButton(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            text = stringResource(R.string.sign_up),
            onClick = onSignUpClick,
            isLoading = isLoading,
            enabled = errorSignUpForm(
                nameError,
                emailError,
                phoneError,
                passwordError
            ) && !isLoading,
        )
        Spacer(Modifier.height(16.dp))
        RealTimeChatLink(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = stringResource(R.string.already_have_an_account_sign_in),
            onClick = onSignInClick
        )
    }
}

@Composable
private fun SignUpEvent(
    uiEvent: Flow<UiEvent>,
    snackBarHostState: SnackbarHostState,
    onSignUpClick: () -> Unit,
    onSignInClick: () -> Unit,
) {

    val context = LocalContext.current
    LaunchedEffect(uiEvent) {
        uiEvent.collectLatest { event ->
            when (event) {
                is UiEvent.NavEvent.SignUpScreen -> onSignUpClick()
                is UiEvent.NavEvent.SignInScreen -> onSignInClick()
                is UiEvent.ShowSnackBar -> {
                    val message = event.message.asString(context)
                    snackBarHostState.showSnackbar(message)
                }

                is UiEvent.CombinedEvents -> {
                    combinedEvent(
                        event = event.event,
                        onShowMessage = { message ->
                            val message = message.asString(context)
                            snackBarHostState.showSnackbar(message)
                        },
                        onNavigate = { navEvent ->
                            when (navEvent) {
                                is UiEvent.NavEvent.SignUpScreen -> onSignUpClick()
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

private fun errorSignUpForm(
    nameError: UiText?,
    emailError: UiText?,
    phoneError: UiText?,
    passwordError: UiText?,
): Boolean {
    return (nameError == null && emailError == null && phoneError == null && passwordError == null)
}
