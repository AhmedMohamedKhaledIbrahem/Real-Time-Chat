package com.example.realtimechat.core.notification.module

import com.example.realtimechat.core.notification.RealTimeChatNotification
import com.example.realtimechat.core.notification.RealTimeChatNotificationImpl
import org.koin.dsl.module

val realTimeChatNotificationModule = module {
    single<RealTimeChatNotification> { RealTimeChatNotificationImpl(get()) }
}