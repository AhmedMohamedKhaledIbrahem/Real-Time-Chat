package com.example.realtimechatapp.features.authentication.data.resource.remote

import com.example.realtimechatapp.core.error.AuthDataError
import com.example.realtimechatapp.core.firebase.FirebaseInstance
import com.example.realtimechatapp.core.utils.Result
import com.example.realtimechatapp.features.authentication.data.model.SignUpModel
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.realtimechatapp.BuildConfig
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class AuthenticationRemoteDataSourceTest {

    private val firebaseInstance: FirebaseInstance = mockk()
    private val firebaseAuth: FirebaseAuth = mockk()
    private val firebaseDatabase: FirebaseDatabase = mockk()
    val databaseReference = mockk<DatabaseReference>(relaxed = true)
    val userReference = mockk<DatabaseReference>(relaxed = true)
    private val authResult: AuthResult = mockk()
    private val firebaseUser: FirebaseUser = mockk()
    private lateinit var authenticationRemoteDataSource: AuthenticationRemoteDataSource

    @Before
    fun setUp() {
        authenticationRemoteDataSource = AuthenticationRemoteDataSourceImpl(firebaseInstance)
        every { firebaseInstance.firebaseAuth() } returns firebaseAuth
        every { firebaseInstance.firebaseDatabase() } returns firebaseDatabase
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    private fun mockEmailAndPassword(): Pair<String, String> {
        return Pair("test@example.com", "password123")
    }

    private fun mockSignUpModel(): SignUpModel {
        return SignUpModel(
            name = "John Doe",
            email = "test@example.com",
            phoneNumber = "1234567890",
            password = "password123"
        )
    }

    // Test signIn method - Success case with verified email
    @Test
    fun `signIn Should Return Success True When Email Is Verified`() {
        runBlocking {
            // Given
            coEvery {
                firebaseAuth.signInWithEmailAndPassword(
                    mockEmailAndPassword().first,
                    mockEmailAndPassword().second,
                )
            } returns Tasks.forResult(authResult)
            every { authResult.user } returns firebaseUser
            every { firebaseUser.isEmailVerified } returns true

            // When
            val result = authenticationRemoteDataSource.signIn(
                mockEmailAndPassword().first,
                mockEmailAndPassword().second,
            )

            // Then
            assertTrue(result is Result.Success && result.data)
            verify {
                firebaseAuth.signInWithEmailAndPassword(
                    mockEmailAndPassword().first,
                    mockEmailAndPassword().second,
                )
            }
        }
    }

    // Test signIn method - Success case with unverified email
    @Test
    fun `signIn Should Return Success False When Email Is Not Verified`() {
        runBlocking {
            // Given

            coEvery {
                firebaseAuth.signInWithEmailAndPassword(
                    mockEmailAndPassword().first,
                    mockEmailAndPassword().second,
                )
            } returns Tasks.forResult(authResult)

            every { authResult.user } returns firebaseUser
            every { firebaseUser.isEmailVerified } returns false

            // When
            val result = authenticationRemoteDataSource.signIn(
                mockEmailAndPassword().first,
                mockEmailAndPassword().second,
            )

            // Then
            assertTrue(result is Result.Success)
            assertFalse((result as Result.Success).data)
        }
    }

    //    // Test signIn method - Error case when user is null
    @Test
    fun `signIn Should Return Error When User Is Null`() {
        runBlocking {
            // Given
            val firebaseAuthException = mockk<FirebaseAuthException> {
                every { errorCode } returns AUTH_FAILED_ERROR
            }
            coEvery {
                firebaseAuth.signInWithEmailAndPassword(
                    mockEmailAndPassword().first,
                    mockEmailAndPassword().second,
                )
            } returns Tasks.forException(firebaseAuthException)
            every { authResult.user } returns null

            // When
            val result = authenticationRemoteDataSource.signIn(
                mockEmailAndPassword().first,
                mockEmailAndPassword().second,
            )

            // Then
            assertTrue(result is Result.Error)
            assertEquals(AuthDataError.Network.AUTH_FAILED, (result as Result.Error).error)
        }
    }


    //    // Test signUp method - Success case
    @Test
    fun `signUp Should Return Success With Uid When SignUp Succeeds`() {
        runBlocking {
            // Given
            coEvery {
                firebaseAuth.createUserWithEmailAndPassword(
                    mockSignUpModel().email,
                    mockSignUpModel().password
                )
            } returns Tasks.forResult(authResult)
            every { authResult.user } returns firebaseUser
            every { firebaseUser.uid } returns UID
            every { firebaseDatabase.getReference(BuildConfig.DB_REFERENCE) } returns databaseReference
            every { databaseReference.child(UID) } returns userReference
            every { userReference.setValue(mockSignUpModel()) } returns Tasks.forResult(null as Void?)

            // When
            val result = authenticationRemoteDataSource.signUp(mockSignUpModel())

            // Then
            assertTrue(result is Result.Success)
            assertEquals(UID, (result as Result.Success).data)


        }

    }


    //    // Test signUp method - Error case when user is null
    @Test
    fun `signUp Should Return Error When User Is Null`() {
        runBlocking {
            // Given
            val firebaseAuthException = mockk<FirebaseAuthException> {
                every { errorCode } returns AUTH_FAILED_ERROR
            }
            every {
                firebaseAuth.createUserWithEmailAndPassword(
                    mockSignUpModel().email,
                    mockSignUpModel().password,
                )
            } returns Tasks.forException(firebaseAuthException)
            every { authResult.user } returns null

            // When
            val result = authenticationRemoteDataSource.signUp(mockSignUpModel())

            // Then
            assertTrue(result is Result.Error)
            assertEquals(AuthDataError.Network.AUTH_FAILED, (result as Result.Error).error)
        }
    }

    // Test signUp method - Error case with user collision
    @Test
    fun `signUp Should Return Error When User Already Exists`() {
        runBlocking {
            // Given
            val exception = mockk<FirebaseAuthUserCollisionException>()
            every {
                firebaseAuth.createUserWithEmailAndPassword(
                    mockSignUpModel().email,
                    mockSignUpModel().password
                )
            } returns Tasks.forException(exception)

            // When
            val result = authenticationRemoteDataSource.signUp(mockSignUpModel())

            // Then
            assertTrue(result is Result.Error)
            assertEquals(AuthDataError.Network.USER_ALREADY_EXISTS, (result as Result.Error).error)
        }
    }

    // Test forgotPassword method - Success case
    @Test
    fun forgotPasswordShouldReturnSuccessWhenEmailSent() {
        runBlocking {
            // Given
            val email = "test@example.com"
            every {
                firebaseAuth.sendPasswordResetEmail(email)
            } returns Tasks.forResult(null as Void?)

            // When
            val result = authenticationRemoteDataSource.forgotPassword(email)

            // Then
            assertTrue(result is Result.Success)
            verify { firebaseAuth.sendPasswordResetEmail(email) }
        }
    }

    // Test forgotPassword method - Error case with invalid user
    @Test
    fun forgotPasswordShouldReturnErrorWhenUserNotFound() {
        runBlocking {
            // Given
            val email = "nonexistent@example.com"
            val exception = mockk<FirebaseAuthInvalidUserException>()
            every {
                firebaseAuth.sendPasswordResetEmail(email)
            } returns Tasks.forException(exception)

            // When
            val result = authenticationRemoteDataSource.forgotPassword(email)

            // Then
            assertTrue(result is Result.Error)
            assertEquals(AuthDataError.Network.USER_NOT_FOUND, (result as Result.Error).error)
        }
    }

    // Test sendEmailVerification method - Success case
    @Test
    fun `send Email Verification Should Return Success When User Exists`() {
        runBlocking {
            // Given
            every { firebaseAuth.currentUser } returns firebaseUser
            every { firebaseUser.sendEmailVerification() } returns Tasks.forResult(null as Void?)


            // When
            val result = authenticationRemoteDataSource.sendEmailVerification()

            // Then
            assertTrue(result is Result.Success)
            verify { firebaseUser.sendEmailVerification() }
        }
    }

    // Test sendEmailVerification method - Error case when no user logged in
    @Test
    fun `send Email Verification Should Return Error When No User LoggedIn`() {
        runBlocking {
            // Given
            val exception = mockk<FirebaseAuthException> {
                every { errorCode } returns NO_USER_ERROR
            }
            every { firebaseAuth.currentUser } throws exception

            // When
            val result = authenticationRemoteDataSource.sendEmailVerification()

            // Then
            assertTrue(result is Result.Error)
            assertEquals(AuthDataError.Network.NO_USER_LOGGED_IN, (result as Result.Error).error)
        }
    }

    companion object {
        private const val AUTH_FAILED_ERROR = "AUTH_FAILED"
        private const val NO_USER_ERROR = "NO_USER"
        private const val UID = "UID_123_TEST"
    }
}