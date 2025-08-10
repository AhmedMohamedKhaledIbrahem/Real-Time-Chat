package com.example.realtimechat.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.realtimechat.core.database.data.dao.chat_request.ChatRequestDao
import com.example.realtimechat.core.database.data.dao.user.UserDao
import com.example.realtimechat.core.database.data.entity.user.UserEntity

@Database(
    entities = [
        UserEntity::class,
    ],
    version = 2,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun chatRequestDao(): ChatRequestDao
}