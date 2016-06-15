package com.example.keigo_yamamoto.repbyimg

import android.app.Application
import android.content.Context
import com.example.keigo_yamamoto.repbyimg.Main.ImageList
import io.realm.Realm
import io.realm.RealmConfiguration

class RepByImage: Application() {


    override fun onCreate() {
        super.onCreate()
        val  realmConfiguration = RealmConfiguration.Builder(this).build()
        Realm.setDefaultConfiguration(realmConfiguration)
        val imageList = ImageList(this)
    }
}