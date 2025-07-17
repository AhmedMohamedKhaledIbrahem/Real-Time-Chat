package com.example.realtimechatapp.features.authentication.data.resource.local

import android.database.sqlite.SQLiteException
import com.example.realtimechatapp.core.database.data.dao.user.UserDao
import com.example.realtimechatapp.core.database.data.entity.user.UserEntity
import com.example.realtimechatapp.core.error.AuthDataError
import com.example.realtimechatapp.core.utils.Result
import com.example.realtimechatapp.features.authentication.data.mapper.toUserEntity
import com.example.realtimechatapp.features.authentication.data.model.SignUpModel
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import java.io.IOException

class AuthenticationLocalDataSourceTest {

    private lateinit var userDao: UserDao
    private lateinit var authenticationLocalDataSource: AuthenticationLocalDataSource

    @Before
    fun setUp() {
        userDao = mockk()
        authenticationLocalDataSource = AuthenticationLocalDataSourceImpl(userDao)
        mockkStatic(PATH_SIGN_UP_MAPPER)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    // Test saveUser method - Success case
    @Test
    fun saveUserShouldReturnSuccessWhenDaoInsertionSucceeds() {
        runBlocking {
            // Given
            val signUpModel = SignUpModel(
                name = "John Doe",
                email = "test@example.com",
                phoneNumber = "1234567890",
                password = "password123"
            )
            val uid = "test-uid"
            val userEntity = mockk<UserEntity>()

            every { signUpModel.toUserEntity(uid) } returns userEntity
            coEvery { userDao.insertUser(userEntity) } just Runs

            // When
            val result = authenticationLocalDataSource.saveUser(signUpModel, uid)

            // Then
            assertTrue(result is Result.Success)
            coVerify { userDao.insertUser(userEntity) }
        }
    }

    // Test saveUser method - Error case
    @Test
    fun saveUserShouldReturnErrorWhenDaoInsertionFails() {
        runBlocking {
            // Given
            val signUpModel = SignUpModel(
                name = "John Doe",
                email = "test@example.com",
                phoneNumber = "1234567890",
                password = "password123"
            )
            val uid = "test-uid"
            val userEntity = mockk<UserEntity>()
            val exception = SQLiteException("Database error")


            every { signUpModel.toUserEntity(uid) } returns userEntity
            coEvery { userDao.insertUser(userEntity) } throws exception

            // When
            val result = authenticationLocalDataSource.saveUser(signUpModel, uid)

            // Then
            assertTrue(result is Result.Error)
            assertEquals(AuthDataError.Local.DATABASE_ERROR, (result as Result.Error).error)
        }
    }

    // Test getUser method - Success case
    @Test
    fun getUserShouldReturnSuccessWithFlowWhenDaoSucceeds() {
        runBlocking {
            // Given
            val userEntity = mockk<UserEntity>()
            val userFlow = flowOf(userEntity)
            coEvery { userDao.getUser() } returns userFlow

            // When

            val result = authenticationLocalDataSource.getUser()

            // Then
            assertTrue(result is Result.Success)
            assertEquals(userFlow, (result as Result.Success).data)

            coVerify(exactly = 1) { userDao.getUser() }
        }
    }

    // Test getUser method - Error case
    @Test
    fun getUserShouldReturnErrorWhenDaoFails() {
        runBlocking {
            // Given
            val exception = IOException("Read failed")
            coEvery { userDao.getUser() } throws exception

            // When
            val result = authenticationLocalDataSource.getUser()

            // Then
            assertTrue(result is Result.Error)
            assertEquals(AuthDataError.Local.READ_FAILED, (result as Result.Error).error)
        }
    }

    // Test deleteUser method - Success case
    @Test
    fun deleteUserShouldReturnSuccessWhenDaoDeletionSucceeds() {
        runBlocking {
            // Given
            coEvery { userDao.deleteUser() } just Runs

            // When
            val result = authenticationLocalDataSource.deleteUser()

            // Then
            assertTrue(result is Result.Success)
            coVerify { userDao.deleteUser() }
        }
    }

    // Test deleteUser method - Error case
    @Test
    fun deleteUserShouldReturnErrorWhenDaoDeletionFails() {
        runBlocking {
            // Given
            val exception = SQLiteException("Database error")
            coEvery { userDao.deleteUser() } throws exception

            // When
            val result = authenticationLocalDataSource.deleteUser()

            // Then
            assertTrue(result is Result.Error)
            assertEquals(AuthDataError.Local.DATABASE_ERROR, (result as Result.Error).error)
        }
    }

    // Test updateUser method - Success case
    @Test
    fun updateUserShouldReturnSuccessWhenDaoUpdateSucceeds() {
        runBlocking {
            // Given
            val userEntity = mockk<UserEntity>()
            coEvery { userDao.updateUser(userEntity) } just Runs

            // When
            val result = authenticationLocalDataSource.updateUser(userEntity)

            // Then
            assertTrue(result is Result.Success)
            coVerify { userDao.updateUser(userEntity) }
        }
    }

    // Test updateUser method - Error case
    @Test
    fun updateUserShouldReturnErrorWhenDaoUpdateFails() {
        runBlocking {
            // Given
            val userEntity = mockk<UserEntity>()
            val exception = SQLiteException("Database error")
            coEvery { userDao.updateUser(userEntity) } throws exception

            // When
            val result = authenticationLocalDataSource.updateUser(userEntity)

            // Then
            assertTrue(result is Result.Error)
            assertEquals(AuthDataError.Local.DATABASE_ERROR, (result as Result.Error).error)
        }
    }

    // Test activeUserByEmail method - Success case
    @Test
    fun activeUserByEmailShouldReturnSuccessWhenDaoUpdateSucceeds() {
        runBlocking {
            // Given
            val email = "test@example.com"
            coEvery { userDao.updateIsVerifiedByEmail(email) } just Runs

            // When
            val result = authenticationLocalDataSource.activeUserByEmail(email)

            // Then
            assertTrue(result is Result.Success)
            coVerify { userDao.updateIsVerifiedByEmail(email) }
        }
    }

    // Test activeUserByEmail method - Error case
    @Test
    fun activeUserByEmailShouldReturnErrorWhenDaoUpdateFails() {
        runBlocking {
            // Given
            val email = "test@example.com"
            val exception = IOException("Read failed")
            coEvery { userDao.updateIsVerifiedByEmail(email) } throws exception

            // When
            val result = authenticationLocalDataSource.activeUserByEmail(email)

            // Then
            assertTrue(result is Result.Error)
            assertEquals(AuthDataError.Local.READ_FAILED, (result as Result.Error).error)
        }
    }
    @Test
    fun `isUserExist should return success when dao returns user entity`(){
        runBlocking {
            // Given
            val userEntity = mockk<UserEntity>()
            coEvery { userDao.isUserExist() } returns userEntity
            // When
            val result = authenticationLocalDataSource.isUserExist()

            // Then
            assertTrue(result is Result.Success)
            assertEquals(userEntity, (result as Result.Success).data)
        }
    }
    @Test
    fun `isUserExist should return success when dao returns user entity is null`(){
        runBlocking {
            // Given
            coEvery { userDao.isUserExist() } returns null
            // When
            val result = authenticationLocalDataSource.isUserExist()

            // Then
            assertTrue(result is Result.Success)
            assertNull((result as Result.Success).data)
        }
    }
    @Test
    fun`isUserExist should return error when dao throws exception`(){
        runBlocking {
            // Given
            val exception = SQLiteException()
            coEvery { userDao.isUserExist() } throws exception
            // When
            val result = authenticationLocalDataSource.isUserExist()

            // Then
            assertTrue(result is Result.Error)
            assertEquals(AuthDataError.Local.DATABASE_ERROR, (result as Result.Error).error)
        }
    }

    companion object {
        private const val PATH_SIGN_UP_MAPPER =
            "com.example.realtimechatapp.features.authentication.data.mapper.SignUpMapperKt"
    }
}