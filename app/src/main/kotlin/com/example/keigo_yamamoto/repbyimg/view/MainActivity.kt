package com.example.keigo_yamamoto.repbyimg.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.FileProvider
import android.util.Log
import android.view.ContextMenu
import android.view.View
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import com.example.keigo_yamamoto.repbyimg.view.KeywodInputFragment
import com.example.keigo_yamamoto.repbyimg.view.KeywordInputReceiver
import com.example.keigo_yamamoto.repbyimg.presenter.ImageGridAdapter
import com.example.keigo_yamamoto.repbyimg.R
import com.example.keigo_yamamoto.repbyimg.presenter.ImageList
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.File

class MainActivity : Activity(), SearchView.OnQueryTextListener, KeywordInputReceiver {

    lateinit var gridAdapter: ImageGridAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gridAdapter = ImageGridAdapter(this)

        search_box.setOnQueryTextListener(this)
        search_box.isSubmitButtonEnabled = false
        search_box.setIconifiedByDefault(false)

        imagesGrid.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    val imageFileName = (parent.getItemAtPosition(position) as File).name

                    val contentUri = ImageList.getInstance(this).createTmpImage(imageFileName)

                    val intent = Intent(Intent.ACTION_SEND)
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    intent.type = "image/jpeg"
                    intent.putExtra(Intent.EXTRA_STREAM, contentUri)

                    startActivityForResult(intent, 0)
                }

        registerForContextMenu(imagesGrid)

        imagesGrid.adapter = gridAdapter

        val toolbar = findViewById(R.id.toolbar) as Toolbar?
        setActionBar(toolbar)

    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu?.setHeaderTitle(R.string.edit_dialog_title)
        menu?.add(0,0,0, R.string.menu_item_edit)
        menu?.add(0,1,1, R.string.menu_item_delete)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val imageList = ImageList.getInstance(this)
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        val targetFile = gridAdapter.getItem(info.position) as File

        when(item.itemId) {
            0 -> updateImage(targetFile.name)
            1 -> imageList.deleteImage(targetFile.name)
            else -> Log.i("contextmenu", "something")
        }
        gridAdapter.notifyDataSetChanged()
        return false
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        gridAdapter.query = newText
        gridAdapter.notifyDataSetChanged()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onReceiveInput(bundle: Bundle?) {
        val targetFileName = bundle?.getString("filename")
        val newKeyword = bundle?.getString("inputKeyword")
        ImageList.getInstance(this).updateImage(targetFileName, newKeyword)
    }

    private fun updateImage(filename: String?) {
        val bundle = Bundle()
        bundle.putString("filename", filename)

        val dialogFragmaent = KeywodInputFragment()
        Log.i("bundle?", bundle.toString())
        dialogFragmaent.arguments = bundle
        dialogFragmaent.show(fragmentManager, "dialog")
    }

}
