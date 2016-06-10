package com.example.keigo_yamamoto.repbyimg.Main

import android.content.Context
import android.util.Log
import com.example.keigo_yamamoto.repbyimg.ResuImage
import io.realm.Realm
import io.realm.RealmResults
import java.io.File

class ImageList(context: Context?) {


    val realm = Realm.getDefaultInstance()
    val dir = File(context?.filesDir, "images")

    fun getImages(query: String?): Array<File>? {

        val realmResults = if (query == null) {
            getAllImages()
        } else {
            getSelectedImages(query)
        }

        val files = realmResults?.map{ File(dir, it.filename) }?.toTypedArray()

        return files?: null
    }


    private fun getAllImages(): RealmResults<ResuImage>? {
        return realm.where(ResuImage::class.java).findAll()
    }

    private fun getSelectedImages(query: String): RealmResults<ResuImage>? {
        return realm.where(ResuImage::class.java).contains("keyword", query).findAll()
    }

}