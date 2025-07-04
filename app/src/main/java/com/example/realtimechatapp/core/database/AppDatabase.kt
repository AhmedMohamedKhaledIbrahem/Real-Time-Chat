package com.example.realtimechatapp.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.realtimechatapp.core.database.data.dao.user.UserDao
import com.example.realtimechatapp.core.database.data.entity.user.UserEntity

@Database(
    entities = [
        UserEntity::class,
    ],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}