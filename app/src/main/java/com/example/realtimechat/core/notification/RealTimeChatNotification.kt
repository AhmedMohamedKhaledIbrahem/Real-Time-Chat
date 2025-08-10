package com.example.realtimechat.core.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.example.realtimechat.R
import com.example.realtimechat.core.broadcast.AddRequestBroadCastReceiver

abstract class RealTimeChatNotification {
    protected abstract val notificationManager: NotificationManager

    protected abstract fun notificationChannel(): NotificationChannel
    abstract fun showNotification(message: String)
    protected abstract fun intent(intentAction: String, cls: Class<*>): Intent
    protected abstract fun notificationBuilder(message: String): Notification
}

class RealTimeChatNotificationImpl(private val context: Context) : RealTimeChatNotification() {
    override val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override fun notificationChannel(): NotificationChannel {
        val soundUrl = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        return NotificationChannel(
            CHANNEL_ID,
            NOTIFICATION_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            enableLights(true)
            lightColor = 0xFFFFFFFF.toInt()
            setSound(soundUrl, audioAttributes)
        }
    }

    override fun showNotification(message: String) {
        val channel = notificationChannel()
        notificationManager.createNotificationChannel(channel)
        val notification = notificationBuilder(message = message)
        notificationManager.notify(1, notification)
    }

    override fun intent(intentAction: String, cls: Class<*>): Intent {
        return Intent(context, cls).apply {
            action = intentAction
        }
    }

    override fun notificationBuilder(message: String): Notification {
        val title = context.getString(R.string.content_title)
        val body = context.getString(R.string.content_body).format(message)
        val acceptIntent =
            intent(context.getString(R.string.Accept), AddRequestBroadCastReceiver::class.java)
        val rejectIntent =
            intent(context.getString(R.string.Reject), AddRequestBroadCastReceiver::class.java)
        val acceptTitle = context.getString(R.string.Accept)
        val rejectTitle = context.getString(R.string.Reject)
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.notifications)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(
                -1,
                acceptTitle,
                PendingIntent.getBroadcast(
                    context,
                    0,
                    acceptIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
            .addAction(
                -1,
                rejectTitle,
                PendingIntent.getBroadcast(
                    context,
                    1,
                    rejectIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
            .setAutoCancel(true)
            .build()
    }

    companion object {
        private const val CHANNEL_ID = "RealTimeChatChannel_105"
        private const val NOTIFICATION_NAME = "RealTimeChat"

    }
}