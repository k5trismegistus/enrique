package com.example.keigo_yamamoto.repbyimg.Main

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.FileProvider
import android.widget.Toolbar
import android.view.View
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.GridView
import android.widget.SearchView
import com.example.keigo_yamamoto.repbyimg.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.File

class MainActivity : Activity(), SearchView.OnQueryTextListener {

    lateinit var gridAdapter: ImageGridAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gridAdapter = ImageGridAdapter(this)

        search_box.setOnQueryTextListener(this)
        search_box.isSubmitButtonEnabled = false

        imagesGrid.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    val imageFile = parent.getItemAtPosition(position) as File

                    val contentUri = FileProvider
                            .getUriForFile(this, "com.example.keigo_yamamoto.repbyimg.fileprovider", imageFile)

                    val intent = Intent(Intent.ACTION_SEND)
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    intent.type = contentResolver.getType(contentUri)
                    intent.putExtra(Intent.EXTRA_STREAM, contentUri)

                    startActivity(intent)
                }
        imagesGrid.adapter = gridAdapter

        val toolbar = findViewById(R.id.toolbar) as Toolbar?
        setActionBar(toolbar)

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
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
