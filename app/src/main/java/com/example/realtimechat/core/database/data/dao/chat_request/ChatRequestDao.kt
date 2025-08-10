package com.example.realtimechat.core.database.data.dao.chat_request

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.realtimechat.core.database.data.entity.chat_request.ChatRequestEntity

@Dao
interface ChatRequestDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatRequest(chatRequest: ChatRequestEntity)
    @Query("select * from chat_request")
    suspend fun getChatRequests(): List<ChatRequestEntity?>
    @Query("delete from chat_request")
    suspend fun deleteChatRequest()

}