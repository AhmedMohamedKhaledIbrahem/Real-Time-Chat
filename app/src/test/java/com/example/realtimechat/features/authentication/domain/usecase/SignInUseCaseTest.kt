package com.example.realtimechat.features.authentication.domain.usecase

import com.example.realtimechat.core.error.DomainError
import com.example.realtimechat.core.utils.Result
import com.example.realtimechat.features.authentication.domain.repository.AuthenticationRepository
import com.example.realtimechat.features.authentication.domain.usecase.signin.SignInUseCase
import com.example.realtimechat.features.authentication.domain.usecase.signin.SignInUseCaseImpl
import com.example.realtimechat.features.authentication.domain.validator.Validator
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SignInUseCaseTest {
    private val repository = mockk<AuthenticationRepository>()
    private val regexValidator = mockk<Validator>()
    private lateinit var useCase: SignInUseCase

    @Before
    fun setUp() {
        useCase = SignInUseCaseImpl(repository = repository, validator = regexValidator)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `signIn should call repository's signIn method with correct parameters`() {
        runBlocking {
            val email = "test@example.com"
            val password = "password123"
            coEvery { repository.signIn(email, password) } returns Result.Success(Unit)
            val result = useCase.invoke(email, password)
            assertTrue(result is Result.Success)
            coVerify(exactly = 1) { repository.signIn(email, password) }
        }
    }

    @Test
    fun `signIn should return error when email or password is blank`() {
        runBlocking {
            val email = ""
            val password = ""
            coEvery {
                repository.signIn(email, password)
            } returns Result.Error(DomainError.Network.INVALID_LOGIN_PARAMS)
            val result = useCase.invoke(email, password)
            assertTrue(
                result is Result.Error &&
                        result.error == DomainError.Network.INVALID_LOGIN_PARAMS
            )
            coVerify(exactly = 0) { repository.signIn(email, password) }
        }
    }
    @Test
    fun `signIn should return error when email is not contains @`() {
        runBlocking {
            val email = "sda23.com"
            val password = "password123"
            coEvery {
                repository.signIn(email, password)
            } returns Result.Error(DomainError.Network.INVALID_EMAIL)
            val result = useCase.invoke(email, password)
            assertTrue(
                result is Result.Error &&
                        result.error == DomainError.Network.INVALID_EMAIL
            )
            coVerify(exactly = 0) { repository.signIn(email, password) }
        }
    }
}