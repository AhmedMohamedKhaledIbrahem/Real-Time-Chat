package com.example.realtimechat.core.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

abstract class Notification {
    protected abstract val notificationManager: NotificationManager
    protected abstract fun notificationChannel(): NotificationChannel
    abstract fun showNotification(message: String)
    protected abstract fun notificationBuilder(message: String): Notification
}

class NotificationImpl(private val context: Context) : Notification() {
    override val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override fun notificationChannel(): NotificationChannel {
        TODO("Not yet implemented")
    }

    override fun showNotification(message: String) {
        TODO("Not yet implemented")
    }

    override fun notificationBuilder(message: String): Notification {
        TODO("Not yet implemented")
    }

    companion object {
        private const val CHANNEL_ID = ""
    }
}