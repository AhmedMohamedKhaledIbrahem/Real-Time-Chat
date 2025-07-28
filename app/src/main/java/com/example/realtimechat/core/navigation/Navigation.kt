package com.example.realtimechat.core.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.example.realtimechat.core.event.UiEvent
import com.example.realtimechat.core.ui.SplashScreenRoot
import com.example.realtimechat.features.authentication.presentation.controller.forgetpassword.ForgetPasswordViewModel
import com.example.realtimechat.features.authentication.presentation.controller.is_logged.IsLoggedViewModel
import com.example.realtimechat.features.authentication.presentation.controller.signin.SignInViewModel
import com.example.realtimechat.features.authentication.presentation.controller.signup.SignUpViewModel
import com.example.realtimechat.features.authentication.presentation.screen.ForgetPasswordScreenRoot
import com.example.realtimechat.features.authentication.presentation.screen.SignInScreenRoot
import com.example.realtimechat.features.authentication.presentation.screen.SignUpScreenRoot
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel

typealias splashScreen = UiEvent.NavEvent.SplashScreen
typealias signInScreen = UiEvent.NavEvent.SignInScreen
typealias signUpScreen = UiEvent.NavEvent.SignUpScreen
typealias forgetPasswordScreen = UiEvent.NavEvent.ForgetPasswordScreen
typealias homeScreen = UiEvent.NavEvent.HomeScreen

@Composable
fun NavigationRoot(
    snackbarHostState: SnackbarHostState,
    ) {
    val backStack = rememberNavBackStack(splashScreen)
    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
            rememberSceneSetupNavEntryDecorator(),
        ),
        entryProvider = { key ->
            when (key) {
                is UiEvent.NavEvent.SplashScreen -> {
                    NavEntry(
                        key = key
                    ){
                        val isLoggedViewModel = koinViewModel<IsLoggedViewModel>()
                        SplashScreenRoot(
                            isLoggedViewModel = isLoggedViewModel,
                            snackbarHostState = snackbarHostState,
                            navigationToHome = { backStack.add(homeScreen) },
                            navigationToLogin = { backStack.add(signInScreen) }
                        )
                    }
                }
                is UiEvent.NavEvent.SignInScreen -> {
                    NavEntry(
                        key = key,
                    ) {
                        val signInViewModel = koinViewModel<SignInViewModel>()
                        SignInScreenRoot(
                            signInViewModel = signInViewModel,
                            snackBarHostState = snackbarHostState,
                            onForgetPasswordClick = { backStack.add(forgetPasswordScreen) },
                            onSignUpClick = { backStack.add(signUpScreen) },
                            onHomeScreenClick = { backStack.add(homeScreen) }
                        )
                    }
                }

                is UiEvent.NavEvent.SignUpScreen -> {
                    NavEntry(
                        key = key,
                    ) {
                        val signUpViewModel = koinViewModel<SignUpViewModel>()
                        SignUpScreenRoot(
                            signUpViewModel = signUpViewModel,
                            snackBarHostState = snackbarHostState,
                            onSignUpClick = { backStack.add(signUpScreen) },
                            onSignInClick = { backStack.add(signInScreen) },
                        )
                    }
                }

                is UiEvent.NavEvent.ForgetPasswordScreen -> {
                    NavEntry(
                        key = key,
                    ) {
                        val forgetPasswordViewModel = koinViewModel<ForgetPasswordViewModel>()
                        ForgetPasswordScreenRoot(
                            forgetPasswordViewModel = forgetPasswordViewModel,
                            snackbarHostState = snackbarHostState,
                            onForgetPasswordClick = {
                                backStack.add(signInScreen)
                            }
                        )
                    }
                }

                is UiEvent.NavEvent.HomeScreen -> {
                    NavEntry(
                        key = key,
                    ) {

                    }
                }

                else -> throw RuntimeException("Invalid NavKey: $key")
            }

        }
    )

}

