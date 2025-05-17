package com.daily.news.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.daily.news.R
import com.daily.news.view.DashboardActivity // Or any other Activity you want to open
import com.daily.news.view.FeedsFragment
import com.daily.news.view.SplashActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val title = remoteMessage.notification?.title ?: "Notification"
        val message = remoteMessage.notification?.body ?: ""

        val action = remoteMessage.data["action"]
        if (action == "open_details") {
            // If action is "open_details", open DetailActivity
            val intent = Intent(this, DashboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            // Create a PendingIntent
            val pendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            // Show notification with the PendingIntent
            showNotification(title, message, pendingIntent)
        } else {
            // If no matching action, you can fallback to opening a default activity like DashboardActivity
            val intent = Intent(this, SplashActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val pendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            showNotification(title, message, pendingIntent)
        }

    }

    private fun showNotification(title: String, message: String, pendingIntent: PendingIntent) {
        val channelId = "default_channel_id"
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // For Android 8+ (Oreo and above), create a notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Default Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Build the notification
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.splashlogo) // Your app's icon
            .setContentIntent(pendingIntent) // Set the pending intent that opens the activity when clicked
            .setAutoCancel(true) // Automatically dismiss the notification when clicked
            .build()

        // Show the notification
        notificationManager.notify(0, notification)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // You can send the new token to your server if needed
    }
}
