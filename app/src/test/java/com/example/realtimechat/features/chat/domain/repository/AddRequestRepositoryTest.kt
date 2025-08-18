package com.example.realtimechat.features.chat.domain.repository

import com.example.internet_connection_monitor.network.InternetConnectionMonitor
import com.example.realtimechat.core.error.DomainError
import com.example.realtimechat.core.shared_preference.RealTimeChatSharedPreference
import com.example.realtimechat.core.utils.Result
import com.example.realtimechat.features.chat.data.model.SendFCMRequestModel
import com.example.realtimechat.features.chat.data.repository.request.AddRequestRepositoryImpl
import com.example.realtimechat.features.chat.data.source.local.AddRequestLocalDataSource
import com.example.realtimechat.features.chat.data.source.remote.request.AddRequestRemoteDataSource
import com.example.realtimechat.features.chat.domain.repository.request.AddRequestRepository
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class AddRequestRepositoryTest {
    private val localDataSource = mockk<AddRequestLocalDataSource>()
    private val remoteDataSource = mockk<AddRequestRemoteDataSource>()
    private val sharedPreference = mockk<RealTimeChatSharedPreference>()
    private val internetConnectionMonitor = mockk<InternetConnectionMonitor>()
    lateinit var addRequestRepository: AddRequestRepository

    private fun hasInternet(internet: Boolean){
        coEvery {
            internetConnectionMonitor.hasConnection()
        } returns internet
    }

    @Before
    fun setup(){
        addRequestRepository = AddRequestRepositoryImpl(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource,
            sharedPreference = sharedPreference,
            internetConnectionMonitor = internetConnectionMonitor
        )
    }
    @After
    fun tearDown(){
        clearAllMocks()
    }

    @Test
    fun `sendFcmRequest should send fcm when the internet's online`() {
        runTest {
            hasInternet(true)
            val sendFCMRequestModel = mockk<SendFCMRequestModel>()
            coEvery {
                remoteDataSource.sendFcmRequest(sendFCMRequestModel = sendFCMRequestModel)
            } returns Result.Success(Unit)
            val result = addRequestRepository.sendFcmRequest(sendFCMRequestModel)
            assertTrue(result is Result.Success)
            coVerify(exactly = 1) { remoteDataSource.sendFcmRequest(sendFCMRequestModel) }
        }
    }
    @Test
    fun `sendFcmRequest should return Error when the internet's offline`(){
        runTest {
            val sendFCMRequestModel = mockk<SendFCMRequestModel>()
            hasInternet(false)
            val result = addRequestRepository.sendFcmRequest(sendFCMRequestModel)
            assertTrue(
                result is Result.Error &&
                result.error == DomainError.Network.NETWORK_UNAVAILABLE
            )
        }
    }
    @Test
    fun `sendFcmRequest should return Error when the internet's online`(){
        runTest {
            val sendFCMRequestModel = mockk<SendFCMRequestModel>()
            hasInternet(true)
            val result = addRequestRepository.sendFcmRequest(sendFCMRequestModel)
            assertTrue(
                result is Result.Error &&
                        result.error == DomainError.Network.NETWORK_UNAVAILABLE
            )
        }
    }

}