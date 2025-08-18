package com.example.realtimechat.features.authentication.presentation.controller

import com.example.realtimechat.R
import com.example.realtimechat.core.error.DomainError
import com.example.realtimechat.core.event.UiEvent
import com.example.realtimechat.core.shared_preference.RealTimeChatSharedPreference
import com.example.realtimechat.core.ui_text.UiText
import com.example.realtimechat.core.utils.Result
import com.example.realtimechat.features.MainDispatcherRule
import com.example.realtimechat.features.collectEvent
import com.example.realtimechat.features.activeTestFLow
import com.example.realtimechat.features.authentication.domain.usecase.signin.SignInUseCase
import com.example.realtimechat.features.authentication.presentation.controller.signin.SignInViewModel
import com.example.realtimechat.features.authentication.presentation.controller.signin.event.SignInEvent
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SignInViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private val signInUseCase = mockk<SignInUseCase>()
    private val sharedPreference = mockk<RealTimeChatSharedPreference>()
    private lateinit var viewModel: SignInViewModel

    @Before
    fun setUp() {
        viewModel = SignInViewModel(signInUseCase,sharedPreference)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    private val email = "test@test.com"
    private val password = "123456"

    @Test
    fun `onEvent with Email Input should update email state`() {
        runTest {
            val job = activeTestFLow(viewModel.signInState)
            viewModel.onEvent(SignInEvent.Email(email))
            advanceUntilIdle()
            val state = viewModel.signInState.value.user
            assertEquals(email, state)
            job.cancel()
        }
    }

    @Test
    fun `onEvent with Password Input should update password state`() {
        runTest {
            val job = activeTestFLow(viewModel.signInState)
            viewModel.onEvent(SignInEvent.Password(password))
            advanceUntilIdle()
            val state = viewModel.signInState.value.password
            assertEquals(password, state)
            job.cancel()
        }
    }

    @Test
    fun `onEvent with click signUp should send the navigation signUp event`() {
        runTest {
            val events = mutableListOf<UiEvent>()
            val channelEventAsync = async {
                collectEvent(
                    flow = viewModel.signInEvent,
                    events = events,
                )
            }
            viewModel.onEvent(SignInEvent.Click.SignUp)
            val channelEvent = channelEventAsync.await()
            assertTrue(channelEvent.first() is UiEvent.NavEvent.SignUpScreen)
        }
    }

    @Test
    fun `onEvent with click forgetPassword should send the navigation forgetPassword event`() {
        runTest {
            val events = mutableListOf<UiEvent>()
            val channelEventAsync = async {
                collectEvent(
                    flow = viewModel.signInEvent,
                    events = events,
                )
            }
            viewModel.onEvent(SignInEvent.Click.ForgetPassword)
            val channelEvent = channelEventAsync.await()
            assertTrue(channelEvent.first() is UiEvent.NavEvent.ForgetPasswordScreen)
        }
    }

    @Test
    fun `onEvent with click signIn should call signInUseCase`() {
        runTest {
            val signInSpy = spyk(viewModel, recordPrivateCalls = true)
            signInSpy.onEvent(SignInEvent.Click.SignIn)
            coEvery { signInUseCase.invoke(any(), any()) } returns Result.Success(Unit)
            coVerify(exactly = 1) { signInSpy[SIGN_IN]() }
        }
    }

    @Test
    fun `signIn should navigate to home screen when signIn is successful`() {
        runTest {
            viewModel.onEvent(SignInEvent.Email(email))
            viewModel.onEvent(SignInEvent.Password(password))
            val emailState = viewModel.signInState.value.user.orEmpty()
            val passwordState = viewModel.signInState.value.password.orEmpty()
            coEvery {
                signInUseCase.invoke(
                    emailState,
                    passwordState,
                )
            } returns Result.Success(Unit)
            val eventAsync = async { viewModel.signInEvent.first() }
            viewModel.onEvent(SignInEvent.Click.SignIn)
            advanceUntilIdle()

            coVerify(exactly = 1) { signInUseCase.invoke(emailState, passwordState) }
            val event = eventAsync.await()
            assertTrue(event is UiEvent.NavEvent.HomeScreen)
            assert(!viewModel.signInState.value.isLoading)
        }

    }

    @Test
    fun `signIn should show error message when signIn is user not found`() {
        runTest {
            val expectedError = DomainError.Network.USER_NOT_FOUND
            viewModel.onEvent(SignInEvent.Email(email))
            viewModel.onEvent(SignInEvent.Password(password))
            val emailState = viewModel.signInState.value.user.orEmpty()
            val passwordState = viewModel.signInState.value.password.orEmpty()
            coEvery {
                signInUseCase.invoke(
                    emailState,
                    passwordState,
                )
            } returns Result.Error(expectedError)
            val eventAsync = async { viewModel.signInEvent.first() }
            viewModel.onEvent(SignInEvent.Click.SignIn)
            advanceUntilIdle()
            coVerify(exactly = 1) { signInUseCase.invoke(emailState, passwordState) }
            val event = eventAsync.await()
            val snackbar = event as UiEvent.ShowSnackBar
            assertTrue(snackbar.message is UiText.StringResource)
            assertEquals(
                R.string.error_user_not_found,
                (snackbar.message as UiText.StringResource).resId
            )
        }
    }

    @Test
    fun `signIn should show error message when signIn is invalid login params`() {
        runTest {
            val expectedError = DomainError.Network.INVALID_LOGIN_PARAMS
            viewModel.onEvent(SignInEvent.Email(""))
            viewModel.onEvent(SignInEvent.Password(""))
            val emailState = viewModel.signInState.value.user.orEmpty()
            val passwordState = viewModel.signInState.value.password.orEmpty()
            coEvery {
                signInUseCase.invoke(
                    emailState,
                    passwordState,
                )
            } returns Result.Error(expectedError)
            val eventAsync = async { viewModel.signInEvent.first() }
            viewModel.onEvent(SignInEvent.Click.SignIn)
            advanceUntilIdle()
            coVerify(exactly = 1) { signInUseCase.invoke(emailState, passwordState) }
            val event = eventAsync.await()
            val snackbar = event as UiEvent.ShowSnackBar
            assertTrue(snackbar.message is UiText.StringResource)
            assertEquals(
                R.string.error_invalid_login_params,
                (snackbar.message as UiText.StringResource).resId,
            )
        }
    }


    companion object {
        const val SIGN_IN = "signIn"
    }

}