package com.example.keigo_yamamoto.repbyimg.Main

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.example.keigo_yamamoto.repbyimg.ResuImage
import io.realm.Realm
import io.realm.RealmResults
import java.io.File
import java.io.FileOutputStream

class ImageList(context: Context?) {

    companion object
    {
        private var instance: ImageList? = null
        fun getInstance(context: Context?): ImageList
        {
            if(instance == null)
            {
                instance = ImageList(context)
            }

            return instance!!
        }
    }


    val dir = File(context?.filesDir, "images")
    val context = context

    fun getImages(query: String?): Array<File>? {

        val realmResults = if (query == null) {
            getAllImages()
        } else {
            getSelectedImages(query)
        }

        val files = realmResults?.map { File(dir, it.filename) }?.toTypedArray()

        return files
    }

    fun createImage(keyword: String?, image: Bitmap) {

        val filename = "${System.currentTimeMillis().toString()}.jpg"
        dir.mkdir()
        val fileOutputStream = FileOutputStream(File(dir, filename))

        val os = context?.openFileOutput(filename, Context.MODE_PRIVATE)
        image.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
        os?.close()

        saveToRealm(filename, keyword?:"default")

    }

    fun updateImage(filename: String, newKeyword: String) {
        Log.i("updateImage", filename)
    }

    fun deleteImage(filename: String) {
        File(dir, filename).delete()
        deleteFromRealm(filename)
    }

    private fun saveToRealm(filename: String, keyword: String) {
        val realm = Realm.getDefaultInstance()

        realm.beginTransaction()
        val ri = realm.createObject(ResuImage::class.java)
        ri.filename = filename
        ri.keyword = keyword
        realm.commitTransaction()
    }

    private fun deleteFromRealm(filename: String) {
        val realm = Realm.getDefaultInstance()

        realm.beginTransaction()
        val ri = realm.where(ResuImage::class.java).equalTo("filename", filename).findAll()
        ri.deleteFirstFromRealm()
        realm.commitTransaction()
    }


    private fun getAllImages(): RealmResults<ResuImage>? {
        val realm = Realm.getDefaultInstance()
        return realm.where(ResuImage::class.java).findAll()
    }

    private fun getSelectedImages(query: String): RealmResults<ResuImage>? {
        val realm = Realm.getDefaultInstance()
        return realm.where(ResuImage::class.java).contains("keyword", query).findAll()
    }

}