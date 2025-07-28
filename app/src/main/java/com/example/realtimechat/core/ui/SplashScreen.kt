package com.example.realtimechat.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.realtimechat.core.event.UiEvent
import com.example.realtimechat.features.authentication.presentation.controller.is_logged.IsLoggedViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SplashScreenRoot(
    isLoggedViewModel: IsLoggedViewModel,
    snackbarHostState: SnackbarHostState,
    navigationToHome: () -> Unit,
    navigationToLogin: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LaunchedEffect(Unit) {
            isLoggedViewModel.isLogged()
        }
        CircularProgressIndicator(
            modifier = Modifier.sizeIn(maxHeight = 120.dp, maxWidth = 120.dp),
        )


        IsLoggedInEvent(
            isLoggedInEvent = isLoggedViewModel.isLoggedEvent,
            snackbarHostState = snackbarHostState,
            navigationToHome = navigationToHome,
            navigationToLogin = navigationToLogin
        )
    }
}

@Composable
fun IsLoggedInEvent(
    isLoggedInEvent: Flow<UiEvent>,
    snackbarHostState: SnackbarHostState,
    navigationToHome: () -> Unit,
    navigationToLogin: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        isLoggedInEvent.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackBar -> {
                    val message = event.message.asString(context)
                    snackbarHostState.showSnackbar(message)
                }

                is UiEvent.NavEvent.HomeScreen -> navigationToHome()
                is UiEvent.NavEvent.SignInScreen -> navigationToLogin()

                else -> Unit
            }
        }
    }
}