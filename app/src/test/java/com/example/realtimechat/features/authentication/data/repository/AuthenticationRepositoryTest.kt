package com.example.realtimechat.features.authentication.data.repository

import com.example.internet_connection_monitor.network.InternetConnectionMonitor
import com.example.realtimechat.core.error.DataError
import com.example.realtimechat.core.error.DomainError
import com.example.realtimechat.core.utils.Result
import com.example.realtimechat.features.authentication.data.mapper.toEntity
import com.example.realtimechat.features.authentication.data.mapper.toSignUpModel
import com.example.realtimechat.features.authentication.data.model.SignUpModel
import com.example.realtimechat.features.authentication.data.model.UserModel
import com.example.realtimechat.features.authentication.data.resource.local.AuthenticationLocalDataSource
import com.example.realtimechat.features.authentication.data.resource.remote.AuthenticationRemoteDataSource
import com.example.realtimechat.features.authentication.domain.entity.SignUpEntity
import com.example.realtimechat.features.authentication.domain.repository.AuthenticationRepository
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AuthenticationRepositoryTest {
    private val localDataSource = mockk<AuthenticationLocalDataSource>()
    private val remoteDataSource = mockk<AuthenticationRemoteDataSource>()
    private val internetConnectionMonitor = mockk<InternetConnectionMonitor>()
    private lateinit var authenticationRepository: AuthenticationRepository

    @Before
    fun setUp() {
        authenticationRepository = AuthenticationRepositoryImpl(
            localDataSource,
            remoteDataSource,
            internetConnectionMonitor
        )
        mockkStatic(PATH_SIGN_UP_MAPPER)
        mockkStatic(PATH_SIGN_IN_MAPPER)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    private fun hasConnection(connection: Boolean) {
        coEvery { internetConnectionMonitor.hasConnection() } returns connection
    }

    private fun mockEmailAndPassword(): Pair<String, String> {
        return Pair("email@gmail.com", "password123")
    }

    private val uid = "UID_231"
    private fun mockSignUpEntity(): SignUpEntity {
        return SignUpEntity(
            email = "email@gmail.com",
            password = "password123",
            name = "Test User",
            phoneNumber = "1234567890"
        )


    }

    private fun mockSignUpModel(): SignUpModel {
        return SignUpModel(
            email = "email@gmail.com",
            password = "password123",
            name = "Test User",
            phoneNumber = "1234567890"
        )
    }

    private fun mockUserModel(): UserModel {
        return UserModel(
            email = "email@gmail.com",
            imageUrl = "www.image.com",
            name = "Test User",
            phone = "1234567890"
        )
    }


    @Test
    fun `signIn success when the email is verified and the internet connection is available`() {
        runBlocking {
            val userModel = mockUserModel()
            val signUpModel = mockSignUpModel()
            hasConnection(true)
            coEvery {
                remoteDataSource.signIn(
                    mockEmailAndPassword().first,
                    mockEmailAndPassword().second,
                )
            } returns Result.Success(true)

            coEvery { remoteDataSource.fetchUser() } returns Result.Success(
                Pair(
                    userModel,
                    uid
                )
            )
            every { userModel.toSignUpModel() } returns signUpModel
            coEvery { localDataSource.isUserExist() } returns Result.Success(null)
            coEvery { localDataSource.saveUser(signUpModel, uid) } returns Result.Success(Unit)
            coEvery {
                localDataSource.activeUserByEmail(mockEmailAndPassword().first)
            } returns Result.Success(Unit)

            val result = authenticationRepository.signIn(
                mockEmailAndPassword().first,
                mockEmailAndPassword().second,
            )

            assertTrue(result is Result.Success && result.data == Unit)
            coVerify(exactly = 1) {
                remoteDataSource.signIn(
                    mockEmailAndPassword().first,
                    mockEmailAndPassword().second,
                )
            }
        }
    }

    @Test
    fun `signIn failed when the email is not verified and the internet connection is available`() {
        runBlocking {
            hasConnection(true)
            coEvery {
                remoteDataSource.signIn(
                    mockEmailAndPassword().first,
                    mockEmailAndPassword().second,
                )
            } returns Result.Success(false)
            coEvery { remoteDataSource.sendEmailVerification() } returns Result.Success(Unit)
            val result = authenticationRepository.signIn(
                mockEmailAndPassword().first,
                mockEmailAndPassword().second,
            )
            assertTrue(
                result is Result.Error &&
                        result.error == DomainError.Network.EMAIL_NOT_VERIFIED
            )
            coVerify(exactly = 1) {
                remoteDataSource.signIn(
                    mockEmailAndPassword().first,
                    mockEmailAndPassword().second,
                )
            }
            coVerify(exactly = 1) {
                remoteDataSource.sendEmailVerification()
            }
        }
    }

    @Test
    fun `signIn failed when the internet connection is unavailable`() {
        runBlocking {
            hasConnection(false)
            val result = authenticationRepository.signIn(
                mockEmailAndPassword().first,
                mockEmailAndPassword().second,
            )
            assertTrue(
                result is Result.Error &&
                        result.error == DomainError.Network.NETWORK_UNAVAILABLE
            )
        }
    }

    @Test
    fun `signIn failed when the sendEmailVerification has an exception and network is available`() {
        runBlocking {
            hasConnection(true)
            coEvery {
                remoteDataSource.signIn(
                    mockEmailAndPassword().first,
                    mockEmailAndPassword().second,
                )
            } returns Result.Success(false)
            coEvery {
                remoteDataSource.sendEmailVerification()
            } returns Result.Error(
                DataError.Network.UNKNOWN
            )
            val result = authenticationRepository.signIn(
                mockEmailAndPassword().first,
                mockEmailAndPassword().second,
            )
            assertTrue(
                result is Result.Error &&
                        result.error == DomainError.Network.UNKNOWN
            )
            coVerify(exactly = 1) {
                remoteDataSource.signIn(
                    mockEmailAndPassword().first,
                    mockEmailAndPassword().second,
                )
            }
            coVerify(exactly = 1) {
                remoteDataSource.sendEmailVerification()
            }
        }
    }

    @Test
    fun `signIn failed when the activeUserByEmail has an exception and network is available`() {
        runBlocking {

            hasConnection(true)
            val userModel = mockUserModel()
            val signUpModel = mockSignUpModel()
            coEvery {
                remoteDataSource.signIn(
                    mockEmailAndPassword().first,
                    mockEmailAndPassword().second,
                )
            } returns Result.Success(true)

            coEvery { localDataSource.isUserExist() } returns Result.Success(null)

            coEvery { remoteDataSource.fetchUser() } returns Result.Success(
                Pair(
                    userModel,
                    uid
                )
            )
            every { userModel.toSignUpModel() } returns signUpModel
            coEvery { localDataSource.saveUser(signUpModel, uid) } returns Result.Success(Unit)
            coEvery {
                localDataSource.activeUserByEmail(mockEmailAndPassword().first)
            } returns Result.Error(
                DataError.Local.UNKNOWN
            )
            val result = authenticationRepository.signIn(
                mockEmailAndPassword().first,
                mockEmailAndPassword().second,
            )
            assertTrue(
                result is Result.Error &&
                        result.error == DomainError.Local.UNKNOWN
            )
            coVerify(exactly = 1) {
                remoteDataSource.signIn(
                    mockEmailAndPassword().first,
                    mockEmailAndPassword().second,
                )
            }
            coVerify(exactly = 1) {
                localDataSource.activeUserByEmail(mockEmailAndPassword().first)
            }
        }
    }

    @Test
    fun `signIn should return an error if the remote data source fails and there is internet connection`() {
        runBlocking {
            hasConnection(true)
            coEvery {
                remoteDataSource.signIn(
                    mockEmailAndPassword().first,
                    mockEmailAndPassword().second,
                )
            } returns Result.Error(
                DataError.Network.UNKNOWN
            )
            val result = authenticationRepository.signIn(
                mockEmailAndPassword().first,
                mockEmailAndPassword().second,
            )
            assertTrue(
                result is Result.Error &&
                        result.error == DomainError.Network.UNKNOWN
            )
            coVerify(exactly = 1) {
                remoteDataSource.signIn(
                    mockEmailAndPassword().first,
                    mockEmailAndPassword().second,
                )
            }

        }
    }

    @Test
    fun `signUp should return success when remoteDataSource return success and save user`() {
        runBlocking {
            hasConnection(true)
            val signUpModel = mockSignUpModel()
            val signUpEntity = mockSignUpEntity()

            every { signUpModel.toEntity() } returns signUpEntity
            coEvery {
                remoteDataSource.signUp(signUpModel)
            } returns Result.Success(uid)
            coEvery {
                localDataSource.saveUser(signUpModel, uid)
            } returns Result.Success(Unit)

            val result = authenticationRepository.signUp(signUpModel.toEntity())
            assertTrue(result is Result.Success)
            coVerify(exactly = 1) {
                remoteDataSource.signUp(signUpModel)
            }
            coVerify(exactly = 1) {
                localDataSource.saveUser(signUpModel, uid)
            }

        }
    }

    @Test
    fun `signUp failed when the internet connection is unavailable`() {
        runBlocking {
            hasConnection(false)
            val signUpModel = mockSignUpModel()
            val signUpEntity = mockSignUpEntity()

            every { signUpModel.toEntity() } returns signUpEntity
            val result = authenticationRepository.signUp(signUpModel.toEntity())
            assertTrue(
                result is Result.Error &&
                        result.error == DomainError.Network.NETWORK_UNAVAILABLE
            )
        }
    }

    @Test
    fun `signUp failed when the remoteDataSource signUp failed and has internet connection`() {
        runBlocking {
            hasConnection(true)
            val signUpModel = mockSignUpModel()
            val signUpEntity = mockSignUpEntity()
            every { signUpModel.toEntity() } returns signUpEntity
            coEvery {
                remoteDataSource.signUp(signUpModel)
            } returns Result.Error(DataError.Network.USER_ALREADY_EXISTS)
            val result = authenticationRepository.signUp(signUpModel.toEntity())
            assertTrue(result is Result.Error && result.error == DomainError.Network.USER_ALREADY_EXISTS)
            coVerify { remoteDataSource.signUp(signUpModel) }
        }
    }

    @Test
    fun `signUp failed when the localDataSource saveUser failed and has internet connection`() {
        runBlocking {
            hasConnection(true)
            val signUpModel = mockSignUpModel()
            val signUpEntity = mockSignUpEntity()
            every { signUpModel.toEntity() } returns signUpEntity
            coEvery {
                remoteDataSource.signUp(signUpModel)
            } returns Result.Success(uid)
            coEvery {
                localDataSource.saveUser(signUpModel, uid)
            } returns Result.Error(DataError.Local.DATABASE_ERROR)
            val result = authenticationRepository.signUp(signUpModel.toEntity())
            assertTrue(result is Result.Error && result.error == DomainError.Local.DATABASE_ERROR)
            coVerify { remoteDataSource.signUp(signUpModel) }
            coVerify { localDataSource.saveUser(signUpModel, uid) }
        }
    }

    @Test
    fun `forgetPassword success when the remoteDataSource forgetPassword success and has internet connection`() {
        runBlocking {
            hasConnection(true)
            val email = "test@example.com"
            coEvery {
                remoteDataSource.forgotPassword(email)
            } returns Result.Success(Unit)
            val result = authenticationRepository.forgotPassword(email)
            assertTrue(result is Result.Success)
            coVerify { remoteDataSource.forgotPassword(email) }
        }
    }

    @Test
    fun `forgetPassword fail has no internet connection`() {
        runBlocking {
            hasConnection(false)
            val email = "test@example.com"
            val result = authenticationRepository.forgotPassword(email)
            assertTrue(result is Result.Error && result.error == DomainError.Network.NETWORK_UNAVAILABLE)
        }
    }

    @Test
    fun `forgetPassword fail when the remoteDataSource forgetPassword fail and has internet connection`() {
        runBlocking {
            hasConnection(true)
            val email = "test@example.com"
            coEvery {
                remoteDataSource.forgotPassword(email)
            } returns Result.Error(DataError.Network.USER_NOT_FOUND)
            val result = authenticationRepository.forgotPassword(email)
            assertTrue(result is Result.Error && result.error == DomainError.Network.USER_NOT_FOUND)
            coVerify { remoteDataSource.forgotPassword(email) }
        }
    }


    companion object {
        private const val PATH_SIGN_UP_MAPPER =
            "com.example.realtimechat.features.authentication.data.mapper.SignUpMapperKt"
        private const val PATH_SIGN_IN_MAPPER =
            "com.example.realtimechat.features.authentication.data.mapper.UserMapperKt"
    }
}