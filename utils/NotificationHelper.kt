package softsolutions.com.tutors.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import softsolutions.com.tutors.MainActivity
import softsolutions.com.tutors.R
import softsolutions.com.tutors.model.MessageReceiver
import softsolutions.com.tutors.model.chat.NotificationPayload


class NotificationHelper(val context: Context) {

    companion object {
        private const val CHANNEL_ID = "channel_tutor"
        private const val CHANNEL_NAME = "tutor"
        private const val EXTRA_MSG_RCV = "msgReceiver"
        private const val NOTIFICATION_ID = 1
    }

    fun sendLocalNotification(chatMessage: NotificationPayload) {
        val pendingIntent = buildPendingIntentFromNavigation(chatMessage)
        val notification = buildLetterNotification(chatMessage, pendingIntent!!)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                )
            )
        }
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun buildLetterNotification(
        chatMessage: NotificationPayload,
        pendingIntent: PendingIntent
    ): Notification? {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(chatMessage.senderName)
            .setContentText(chatMessage.message)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .build()
    }

    private fun buildPendingIntentFromNavigation(chatMessage: NotificationPayload): PendingIntent? {
        val bundle = Bundle()
        val receiver = MessageReceiver(
            chatMessage.senderId, chatMessage.senderName, chatMessage.senderImage, chatMessage.conversationId
        )
        bundle.putSerializable(EXTRA_MSG_RCV, receiver)
        return NavDeepLinkBuilder(context)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.dest_chat)
            .setArguments(bundle)
            .setComponentName(MainActivity::class.java)
            .createPendingIntent()
    }
}
