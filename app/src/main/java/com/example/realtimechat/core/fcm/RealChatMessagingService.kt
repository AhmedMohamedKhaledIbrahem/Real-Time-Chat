package com.example.realtimechat.core.fcm

import com.example.realtimechat.core.logger.Logger
import com.example.realtimechat.core.notification.RealTimeChatNotification
import com.example.realtimechat.core.shared_preference.RealTimeChatSharedPreference
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.koin.android.ext.android.inject
import org.koin.core.component.KoinComponent

class RealChatMessagingService : FirebaseMessagingService(), KoinComponent {
    private val sharedPreference: RealTimeChatSharedPreference by inject()
    val notification: RealTimeChatNotification by inject()
    val logger: Logger by inject()
    val job = CoroutineScope(SupervisorJob())
    override fun onMessageReceived(message: RemoteMessage) {
        try {
            val body = message.notification?.body
            val channelId = message.data["channel_id"]
            val mutex = Mutex()
            if (body == null || channelId == null) throw Exception("remote message is null")
            job.launch {
                mutex.withLock {
                    logger.d(channelId)
                    sharedPreference.saveChannelUid(channelId)
                }
            }
            notification.showNotification(
                body
            )
        } catch (e: Exception) {
            logger.e(
                e,
                e.message ?: "Error while receiving notification"
            )
        }


    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        sharedPreference.saveToken(token)
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

}