package com.example.keigo_yamamoto.repbyimg.Download

import android.R
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.IBinder
import android.util.Log
import com.example.keigo_yamamoto.repbyimg.Main.MainActivity
import com.example.keigo_yamamoto.repbyimg.ResuImage
import io.realm.Realm
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import rx.Observable
import rx.Observer
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream

class DownloadService: Service() {

    lateinit var pendingIntent: PendingIntent

    override fun onCreate() {
        pendingIntent = PendingIntent.getService(this, 0, Intent(this, MainActivity::class.java), 0)
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val url = intent?.getStringExtra("url")
        val inputText = intent?.getStringExtra("inputText")
        val context = this

        Observable.create(object: Observable.OnSubscribe<Int> {

            override fun call(subscriber: Subscriber<in Int>) {

                val filename = "${System.currentTimeMillis().toString()}.jpg"

                try {
                    val response = downloadImage(url)
                    val image = BitmapFactory.decodeStream(response.body().byteStream())

                    val imgDir = File(filesDir, "images")
                    imgDir.mkdirs()
                    val fileOutputStream = FileOutputStream(File(imgDir, filename))

                    val os = context.openFileOutput(filename, Context.MODE_PRIVATE)
                    image.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
                    os.close()

                    saveToRealm(filename, inputText?:"default")

                } catch(e: Exception) {
                    Log.e("error", e.toString())
                }
                subscriber.onCompleted()
            }

        })
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(object: Observer<Int> {

            override fun onCompleted() {
                makeNotification("downloaded.")
            }

            override fun onError(e: Throwable) {
                makeNotification("failed to download.")
            }

            override fun onNext(t: Int) {
            }

        })

        return START_STICKY;
    }

    override fun onDestroy() {
        Log.i("TestService", "onDestroy");
    }

    fun downloadImage(url: String?): Response {
        val okhttpclient = OkHttpClient()
        val req = Request.Builder()
                .url(url)
                .get()
                .build()
        val response = okhttpclient.newCall(req).execute()
        return response
    }

    fun saveToRealm(filename: String, keyword: String) {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        var ri = realm.createObject(ResuImage::class.java)
        ri.filename = filename
        ri.keyword = keyword
        realm.commitTransaction()
    }

    fun makeNotification(message: String) {

        val notif = Notification.Builder(this)
                .setContentTitle(getString(com.example.keigo_yamamoto.repbyimg.R.string.app_name))
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_menu_save)
                .setContentIntent(pendingIntent)
                .build()

        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(com.example.keigo_yamamoto.repbyimg.R.string.app_name, notif)

    }

}
