package com.example.keigo_yamamoto.repbyimg.Download

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.example.keigo_yamamoto.repbyimg.KeywordInputReceiver
import com.example.keigo_yamamoto.repbyimg.R

class KeywodInputFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bundle = arguments
        val dialog = AlertDialog.Builder(activity)
        dialog.setTitle(R.string.download_dialog_title)
        val input = EditText(activity)
        dialog.setView(input)

        dialog.setPositiveButton("OK") { dialog: DialogInterface, which: Int ->
            bundle?.putString("inputKeyword", input.text.toString())
            val drActivity = activity as KeywordInputReceiver
            Log.i("bundle", bundle.toString())
            drActivity.onReceiveInput(bundle)
        }

        return dialog.create()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}