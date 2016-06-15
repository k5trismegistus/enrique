package com.example.keigo_yamamoto.repbyimg.Main

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.keigo_yamamoto.repbyimg.R
import com.example.keigo_yamamoto.repbyimg.SquareImageView
import java.io.File
import kotlinx.android.synthetic.main.grid_item.*

class ImageGridAdapter(_context: Context?): BaseAdapter() {

    val context = _context
    val inflater = LayoutInflater.from(context)
    var query: String? = null
    var imageList = ImageList.getInstance(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

        val view: View

        if (convertView == null) {
            view = inflater.inflate(R.layout.grid_item, parent, false)
        } else {
            view = convertView
        }

        val picture_view = view.findViewById(R.id.picture_view) as ImageView
        val myBitmap = BitmapFactory.decodeFile((getItem(position) as File).absolutePath)

        picture_view.setImageBitmap(myBitmap)

        return view
    }

    override fun getItem(position: Int): Any? {
        return getFiles()?.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return getFiles()?.size?: 0
    }

    fun getFiles(): Array<File>? {
        val filepaths = imageList.getImages(query)
        return filepaths
    }


}