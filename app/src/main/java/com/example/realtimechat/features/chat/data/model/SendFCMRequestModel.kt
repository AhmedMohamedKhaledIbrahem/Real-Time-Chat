package com.example.realtimechat.features.chat.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SendFCMRequestModel(
    @SerialName("fcm_token") val fcmToken: String,
    @SerialName("channel_id") val channelId: String,
    val title: String,
    val body: String
)
@Serializable
data class SendFCMRequestResponse(
    val success: Boolean,
    val messageId: String
)
