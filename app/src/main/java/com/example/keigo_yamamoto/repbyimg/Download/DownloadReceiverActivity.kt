package com.example.keigo_yamamoto.repbyimg.Download

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.keigo_yamamoto.repbyimg.KeywordInputReceiver


class DownloadReceiverActivity : Activity(), KeywordInputReceiver {


    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)

        if (Intent.ACTION_SEND.equals(intent.action)) {
            val bundle = Bundle()
            bundle.putString("url", intent.extras?.getCharSequence(Intent.EXTRA_TEXT).toString())

            val dialogFragmaent = KeywodInputFragment()
            dialogFragmaent.arguments = bundle
            dialogFragmaent.show(fragmentManager, "dialog")
        } else {
            this.finish()
        }
    }

    override fun onReceiveInput(bundle: Bundle?) {
        Log.i("bundle!", bundle.toString())
        startDownload(bundle?.getString("url"), bundle?.getString("inputKeyword"))
    }

    private fun startDownload(url: String?, inputKeyword: String?) {
        val intent = Intent(this, DownloadService::class.java)
        intent.putExtra("url", url)
        intent.putExtra("inputKeyword", inputKeyword)
        startService(intent)
        this.finish()
        this.onDestroy()
    }


}
