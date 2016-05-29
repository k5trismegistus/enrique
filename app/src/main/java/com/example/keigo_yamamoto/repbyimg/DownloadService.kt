package com.example.keigo_yamamoto.repbyimg

import android.app.Notification
import android.app.Service
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
        Log.i("TestService", "onStartCommand");
        var notif= Notification.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("onStartCommand")
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(applicationContext)
                .build();
        return START_STICKY;
    }

    override fun onDestroy() {
        Log.i("TestService", "onDestroy");
    }

}
