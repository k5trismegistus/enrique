package com.example.keigo_yamamoto.repbyimg.Download

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View


class DownloadReceiverActivity : Activity() {

    lateinit var url: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Intent.ACTION_SEND.equals(intent.action)) {
            val extras = intent.extras
            url = extras?.getCharSequence(Intent.EXTRA_TEXT).toString()
        } else {
            this.finish()
        }

        val dialogFragmaent = DownloadDialogFragment()
        dialogFragmaent.show(fragmentManager, "dialog")
    }

    fun startDownload(inputText: String) {
        val intent = Intent(this, DownloadService::class.java)
        intent.putExtra("url", url)
        intent.putExtra("inputText", inputText)
        startService(intent)
        this.finish()
    }


}
