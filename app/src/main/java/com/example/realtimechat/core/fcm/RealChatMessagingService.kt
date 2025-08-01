package com.example.realtimechat.core.fcm

import com.example.realtimechat.core.shared_preference.RealTimeChatSharedPreference
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.koin.android.ext.android.inject
import org.koin.core.component.KoinComponent

class RealChatMessagingService : FirebaseMessagingService(), KoinComponent {
    private val sharedPreference: RealTimeChatSharedPreference by inject()
    override fun onMessageReceived(message: RemoteMessage) {
        val title = message.notification?.title
        val body = message.notification?.body

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        sharedPreference.saveToken(token)
    }


}