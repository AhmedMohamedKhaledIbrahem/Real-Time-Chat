package com.example.realtimechat.core.database.di

import androidx.room.Room
import com.example.realtimechat.BuildConfig
import com.example.realtimechat.core.database.AppDatabase
import com.example.realtimechat.core.database.data.dao.user.UserDao
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