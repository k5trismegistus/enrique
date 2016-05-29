package com.example.keigo_yamamoto.repbyimg

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log

class DownloadService: Service() {

    override fun onCreate() {
        Log.i("TestService", "onCreate");
    }


    override fun onBind(intent: Intent?): IBinder? {
        Log.i("TestService", "onBind");
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        var intentMain = Intent(applicationContext, MainActivity::class.java)

        var pendingIntent = PendingIntent
                .getActivity(applicationContext, 0, intentMain, 0)

        Log.i("TestService", "onStartCommand");
        var notif= Notification.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("onStartCommand")
                .setSmallIcon(android.R.drawable.ic_menu_save)
                .setContentIntent(pendingIntent)
                .build()

        var notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(R.string.app_name, notif)

        return START_STICKY;
    }

    override fun onDestroy() {
        Log.i("TestService", "onDestroy");
    }

}
