package com.example.realtimechatapp.core.database.di

import androidx.room.Room
import com.example.realtimechatapp.BuildConfig
import com.example.realtimechatapp.core.database.AppDatabase
import com.example.realtimechatapp.core.database.data.dao.user.UserDao
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            BuildConfig.DB_ROOM_REFERENCE
        ).build()

    }
    single<UserDao> { get<AppDatabase>().userDao() }
}