package com.example.keigo_yamamoto.repbyimg.Main

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.example.keigo_yamamoto.repbyimg.ResuImage
import io.realm.Realm
import io.realm.RealmResults
import java.io.*

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

    fun createTmpImage(filename: String): Uri? {
        val targetFile = File(dir, filename)
        val extFile = targetFile.copyTo(File(context?.externalCacheDir, filename), true, 4096)

        val cr = context?.contentResolver
        val cv = ContentValues()
        cv.put(MediaStore.Images.Media.TITLE, extFile.name)
        cv.put(MediaStore.Images.Media.DISPLAY_NAME, extFile.name)
        cv.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
        cv.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        cv.put(MediaStore.Images.Media.DATA, extFile.absolutePath)
        val uri = cr?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv)

        return uri
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