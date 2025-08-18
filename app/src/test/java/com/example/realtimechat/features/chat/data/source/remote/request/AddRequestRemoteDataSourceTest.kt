package com.example.realtimechat.features.chat.data.source.remote.request

import com.example.realtimechat.BuildConfig
import com.example.realtimechat.core.error.DataError
import com.example.realtimechat.core.firebase.FirebaseInstance
import com.example.realtimechat.core.logger.Logger
import com.example.realtimechat.core.utils.Result
import com.example.realtimechat.features.chat.data.model.AddRequestModel
import com.example.realtimechat.features.chat.data.model.AddRequestStatus
import com.example.realtimechat.features.chat.data.model.SendFCMRequestModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpStatusCode
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.spyk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Before
import org.junit.Test

class AddRequestRemoteDataSourceTest {
    private val logger: Logger = mockk(relaxed = true)
    private lateinit var httpClient: HttpClient
    private lateinit var mockEngine: MockEngine
    private val firebaseInstance: FirebaseInstance = mockk()
    private val firebaseUser: FirebaseUser = mockk()
    private val firebaseDatabase: FirebaseDatabase = mockk()

    private val firebaseAuth: FirebaseAuth = mockk()

    val databaseReference = mockk<DatabaseReference>(relaxed = true)
    private val childRef: DatabaseReference = mockk(relaxed = true)
    private val secondChildRef: DatabaseReference = mockk(relaxed = true)
    private val threeChildRef: DatabaseReference = mockk(relaxed = true)
    private lateinit var addRequestRemoteDataSource: AddRequestRemoteDataSource
    private val query: Query = mockk()
    private val mockTask = mockk<Task<Void>>(relaxed = true)
    private val tReceiverUID = "receiverUID"
    private val tSenderUID = "sender_uid"
    private val channelUId = "channel_uid"

    @Before
    fun setup() {
        every { firebaseInstance.firebaseAuth() } returns firebaseAuth
        every { firebaseInstance.firebaseDatabase() } returns firebaseDatabase
        every { firebaseAuth.currentUser } returns firebaseUser
        every { firebaseUser.uid } returns tSenderUID
        every { firebaseDatabase.getReference(any()) } returns databaseReference
        every { databaseReference.orderByChild(any()) } returns query
        every { query.equalTo(any<String>()) } returns query
        every { databaseReference.child(any()) } returns childRef
        every { childRef.child(any()) } returns secondChildRef
        every { secondChildRef.child(any()) } returns threeChildRef

        mockEngine = MockEngine { request ->
            val url = request.url.encodedPath
            when (url) {
                "/${BuildConfig.SEND_NOTIFICATION}" -> {
                    respond(
                        content = """{"success": true, "messageId": "12345"}""",
                        status = HttpStatusCode.OK,
                        headers = headers {
                            set("Content-Type", "application/json")
                        }
                    )
                }

                else -> {
                    respond(
                        content = "Not Found",
                        status = HttpStatusCode.NotFound,
                    )
                }
            }
        }
        httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }


        addRequestRemoteDataSource = spyk(
            AddRequestRemoteDataSourceImpl(
                logger = logger,
                httpClient = httpClient,
                firebaseInstance = firebaseInstance
            )
        )

    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    private fun tAddRequestModel() = AddRequestModel(
        senderEmail = "test@test.com",
        receiverEmail = "test2@test.com",
        status = AddRequestStatus.PENDING,
        timestamp = 123456789L
    )


    private fun buildDataSourceWithEngine(engine: MockEngine): AddRequestRemoteDataSource {
        val client = HttpClient(engine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
        return spyk(
            AddRequestRemoteDataSourceImpl(
                logger = logger,
                httpClient = client,
                firebaseInstance = firebaseInstance
            )
        )
    }

    @Test
    fun `addRequest should return success when request is successful`() = runTest {
        val taddRequestModel = tAddRequestModel()
        coEvery {
            addRequestRemoteDataSource.receiveUid(taddRequestModel.receiverEmail)
        } returns Result.Success(tReceiverUID)


        every { mockTask.isSuccessful } returns true
        every { mockTask.exception } returns null

        every {
            mockTask.addOnCompleteListener(any())
        } answers {
            firstArg<OnCompleteListener<Void>>().onComplete(mockTask)
            mockTask
        }

        every { secondChildRef.setValue(taddRequestModel) } returns mockTask
        val result = addRequestRemoteDataSource.addRequest(taddRequestModel,channelUId)
        assert(result is Result.Success)
    }

    @Test
    fun `addRequest should return error when receiverUid is not found`() = runTest {
        val taddRequestModel = tAddRequestModel()
        coEvery {
            addRequestRemoteDataSource.receiveUid(taddRequestModel.receiverEmail)
        } returns Result.Error(DataError.Network.USER_NOT_FOUND)
        val result = addRequestRemoteDataSource.addRequest(taddRequestModel,channelUId)
        assert(result is Result.Error)
    }

    @Test
    fun `addRequest should return error when task is not successful`() = runTest {
        val taddRequestModel = tAddRequestModel()
        val fakeException = Exception("Simulated Firebase failure")
        coEvery {
            addRequestRemoteDataSource.receiveUid(taddRequestModel.receiverEmail)
        } returns Result.Success(tReceiverUID)
        every { mockTask.isSuccessful } returns false
        every { mockTask.exception } returns fakeException
        every {
            mockTask.addOnCompleteListener(any())
        } answers {
            firstArg<OnCompleteListener<Void>>().onComplete(mockTask)
            mockTask
        }
        every { secondChildRef.setValue(taddRequestModel) } returns mockTask
        val result = addRequestRemoteDataSource.addRequest(taddRequestModel,channelUId)
        assert(result is Result.Error)

    }

    @Test
    fun `addRequest should return error when the senderUser is null`() = runTest {
        val exception = mockk<FirebaseAuthException> {
            every { errorCode } returns NO_USER_LOGGED_IN_MESSAGE
        }
        every { logger.e(any(), any()) } returns Unit
        every { firebaseAuth.currentUser } throws exception
        val result = addRequestRemoteDataSource.addRequest(tAddRequestModel(),channelUId)
        assert(result is Result.Error)
    }

    @Test
    fun `sendFcmRequest should return success when server responds with 200`() = runTest {
        val request = SendFCMRequestModel(
            fcmToken = "token",
            channelId =  channelUId,
            title = "title",
            body = "body",
        )
        val result = addRequestRemoteDataSource.sendFcmRequest(request)
        assertTrue(result is Result.Success)
    }

    @Test
    fun `sendFcmRequest should return timeout error when server responds with 408`() = runTest {
        val request = SendFCMRequestModel(
            fcmToken = "token",
            channelId =  channelUId,
            title = "title",
            body = "body",
        )
        mockEngine = MockEngine {
            respond("Timeout", HttpStatusCode.RequestTimeout)
        }

        val addRequestRemoteDataSource = buildDataSourceWithEngine(mockEngine)

        val result = addRequestRemoteDataSource.sendFcmRequest(request)
        assertTrue(result is Result.Error)
        assertTrue((result as Result.Error).error == DataError.Network.TIMEOUT)
    }

    @Test
    fun `sendFcmRequest should return unKnown error when server responds not (200 to 299) or 408`() =
        runTest {
            val request = SendFCMRequestModel(
                fcmToken = "token",
                channelId =  channelUId,
                title = "title",
                body = "body",
            )
            mockEngine = MockEngine {
                respond("NotFound", HttpStatusCode.NotFound)
            }
            val addRequestRemoteDataSource = buildDataSourceWithEngine(mockEngine)
            val result = addRequestRemoteDataSource.sendFcmRequest(request)
            assertTrue(result is Result.Error)
            assertTrue((result as Result.Error).error == DataError.Network.UNKNOWN)
        }

    @Test
    fun `addRequestStatus should return success when request is added successfully`() = runTest {
        val receiverEmail = "test@example.com"
        val status = AddRequestStatus.ACCEPTED
        coEvery {
            addRequestRemoteDataSource.receiveUid(receiverEmail)
        } returns Result.Success(tReceiverUID)
        coEvery {
            addRequestRemoteDataSource.senderUid()
        } returns Result.Success(tSenderUID)
        every { mockTask.isSuccessful } returns true
        every { mockTask.exception } returns null

        every {
            mockTask.addOnCompleteListener(any())
        } answers {
            firstArg<OnCompleteListener<Void>>().onComplete(mockTask)
            mockTask
        }
        every { threeChildRef.setValue(status) } returns mockTask
        val result = addRequestRemoteDataSource.addRequestStatus(receiverEmail, status, channelUId = channelUId)
        assertTrue(result is Result.Success)
    }

    @Test
    fun `addRequestStatus should return failure when request fails`() = runTest {
        val receiverEmail = "test@example.com"
        val status = AddRequestStatus.ACCEPTED
        val exception = Exception("Database write failed")

        coEvery {
            addRequestRemoteDataSource.receiveUid(receiverEmail)
        } returns Result.Success(tReceiverUID)
        coEvery {
            addRequestRemoteDataSource.senderUid()
        } returns Result.Success(tSenderUID)
        every { mockTask.isSuccessful } returns false
        every { mockTask.exception } returns exception

        every {
            mockTask.addOnCompleteListener(any())
        } answers {
            firstArg<OnCompleteListener<Void>>().onComplete(mockTask)
            mockTask
        }
        every { threeChildRef.setValue(status) } returns mockTask
        val result = addRequestRemoteDataSource.addRequestStatus(receiverEmail, status,channelUId)
        assertTrue(result is Result.Error)
    }

    @Test
    fun `addRequestStatus should return error when receiverUid is not found`() = runTest {
        val receiverEmail = "test@example.com"
        val status = AddRequestStatus.ACCEPTED
        coEvery {
            addRequestRemoteDataSource.receiveUid(receiverEmail)
        } returns Result.Error(DataError.Network.USER_NOT_FOUND)
        val result = addRequestRemoteDataSource.addRequestStatus(receiverEmail, status,channelUId)
        assertTrue(result is Result.Error)
        assertTrue((result as Result.Error).error == DataError.Network.USER_NOT_FOUND)
    }

    @Test
    fun `addRequestStatus should return error when SenderUid is not found`() = runTest {
        val receiverEmail = "test@example.com"
        val status = AddRequestStatus.ACCEPTED
        coEvery {
            addRequestRemoteDataSource.receiveUid(receiverEmail)
        } returns Result.Success(tReceiverUID)
        coEvery {
            addRequestRemoteDataSource.senderUid()
        } returns Result.Error(DataError.Network.USER_NOT_FOUND)
        val result = addRequestRemoteDataSource.addRequestStatus(receiverEmail, status,channelUId)
        assertTrue(result is Result.Error)
        assertTrue((result as Result.Error).error == DataError.Network.USER_NOT_FOUND)
    }

    @Test
    fun `senderUid should return success when uid is found`() = runTest {
        val result = addRequestRemoteDataSource.senderUid()
        assertTrue(result is Result.Success)
    }

    @Test
    fun `senderUid should return failure when uid is not found`() = runTest {
        val exception = mockk<FirebaseAuthException> {
            every { errorCode } returns NO_USER_ERROR_CODE
        }
        every { logger.e(any(), any()) } returns Unit
        every { firebaseInstance.firebaseAuth().currentUser } throws exception
        val result = addRequestRemoteDataSource.senderUid()
        assertTrue(result is Result.Error)
        assertTrue((result as Result.Error).error == DataError.Network.NO_USER_LOGGED_IN)
    }

    @Test
    fun `receiveUid should return success when uid is found`() = runTest {
        val receiverEmail = "test@example.com"
        val expectedUid = "uid_123"
        val dataSnapshot: DataSnapshot = mockk()
        val childSnapshot: DataSnapshot = mockk()
        every { dataSnapshot.children } returns listOf(childSnapshot)
        every { childSnapshot.key } returns expectedUid
        val slot = slot<ValueEventListener>()
        every { query.addListenerForSingleValueEvent(capture(slot)) } answers {
            slot.captured.onDataChange(dataSnapshot)
        }
        val result = addRequestRemoteDataSource.receiveUid(receiverEmail)
        assertTrue(result is Result.Success)

    }

    @Test
    fun `receiveUid should return error when uid is not found`() = runTest {
        val receiverEmail = "test@example.com"
        val dataSnapshot: DataSnapshot = mockk()

        every { dataSnapshot.children } returns emptyList()
        every { logger.e(any<Throwable>(), eq(NO_USER_LOGGED_IN_MESSAGE)) } returns Unit
        val slot = slot<ValueEventListener>()
        every { query.addListenerForSingleValueEvent(capture(slot)) } answers {
            slot.captured.onDataChange(dataSnapshot)
        }
        val result = addRequestRemoteDataSource.receiveUid(receiverEmail)
        assertTrue(result is Result.Error)
        ((result as Result.Error).error == DataError.Network.USER_NOT_FOUND)
    }
    @Test
    fun `receiveUid should return error when Firebase query is cancelled`() = runTest {
        val receiverEmail = "test@example.com"
        val databaseError: DatabaseError = mockk()
        val exception = DatabaseException("Database cancelled")
        every { databaseError.toException() } returns   exception
        val slot = slot<ValueEventListener>()
        every { query.addListenerForSingleValueEvent(capture(slot)) } answers {
            slot.captured.onCancelled(databaseError)
        }
        val result = addRequestRemoteDataSource.receiveUid(receiverEmail)
        assertTrue(result is Result.Error)
        assertTrue((result as Result.Error).error == DataError.Network.DATABASE_ERROR)
    }


    companion object {
        private const val NO_USER_LOGGED_IN_MESSAGE = "no user logged in"
        private const val NO_USER_ERROR_CODE = "NO_USER"
    }

}