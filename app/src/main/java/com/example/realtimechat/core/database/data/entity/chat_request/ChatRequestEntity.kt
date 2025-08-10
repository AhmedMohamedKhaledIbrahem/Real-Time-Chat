package com.example.realtimechat.core.database.data.entity.chat_request

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_request")
data class ChatRequestEntity(
    @PrimaryKey val uid: String,
    val senderUid: String,
    val senderEmail: String,
    val receiverEmail: String,
    val receiverUid: String,
    val timestamp: Long
) {
    constructor() : this("", "", "", "", "", 0L)
}

