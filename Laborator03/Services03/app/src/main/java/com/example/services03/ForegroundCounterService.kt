package com.example.services03

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.util.Log

class ForegroundCounterService : Service() {

    private val TAG = "ForegroundCounterService"
    private var isCounting = true
    private var count = 0

    //TO DO 3 #starting-a-foreground-service
    // override the onStartCommand and create a notification + start countingThread
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        // define a channel for the notification
        // this is used so that you can group the types of notifications you want to have in your app
        val channelId = "ForegroundServiceChannel"
        val channelName = "Foreground Service"

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)

        //create a notification
        val notification = Notification.Builder(this, channelId)
            .setContentTitle("Notification title")
            .setContentText("Content: $count")
            .setSmallIcon(android.R.drawable.ic_notification_overlay)
            .build()

        //this is when the foreground service actually starts
        startForeground(1, notification)

        //start task ...
        startCountingThread()
        return START_STICKY
    }


    //TO DO 3 #stoping-a-foreground-service
    // override the onDestroy
    override fun onDestroy() {
        super.onDestroy()
        //cleanup ...
        isCounting = false
        Log.d(TAG, "Service stopped")
    }



    private fun startCountingThread() {
        Thread {
            while (isCounting) {
                count++
                Thread.sleep(1000)
                // TO DO 3
                //uncomment this line to call the notification update function
                //updateNotification()
                updateNotification()
            }
        }.start()
    }

    // TO DO 3 #updating-data-in-the-notification
    // create the updateNotification() function that updates the notification with the value of the counter
    //task

    private fun updateNotification() {
        val notification = Notification.Builder(this, "ForegroundServiceChannel")
            .setContentTitle("Notification title")
            .setContentText("Content: $count")
            .setSmallIcon(android.R.drawable.ic_notification_overlay)
            .build()

        //use notification manager to pass the updated notification
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
