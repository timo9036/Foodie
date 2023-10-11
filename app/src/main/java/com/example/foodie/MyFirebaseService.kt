package com.example.foodie

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.foodie.ui.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseService : FirebaseMessagingService() {
    //The method is called every time it receives a notification from Firebase
//The RemoteMessage object returned represents the notification message that was received
    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        // Handle the incoming message and display a notification
        if (remoteMessage.notification != null) {
            // You can customize the notification based on the received message
            val title = remoteMessage.notification?.title
            val body = remoteMessage.notification?.body

            // Create and display a notification
            // You can use your custom logic to show the notification here
            // For simplicity, we'll use the default notification builder
            val notification = NotificationCompat.Builder(this, "1")
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_notification)
                .setAutoCancel(true)
                .build()


            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(1, notification)

            super.onMessageReceived(remoteMessage)
            Log.i("asdf ", "Message :: $remoteMessage")

            // Replace MainActivity with your app's main activity
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val pendingIntent = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
            )
//            val intent = Intent(this, MainActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            startActivity(intent)

        }

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Handle token refresh here if needed
    }
}
