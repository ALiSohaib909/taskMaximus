package com.taskmaximus.utils

import android.app.Activity
import android.app.Dialog
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.taskmaximus.R

class ViewDialog {
    fun showDialog(activity: Activity?, msg: String?) {
        val dialog = activity?.let { Dialog(it) }
        if (dialog != null) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

            dialog.setCancelable(false)
            dialog.setContentView(R.layout.exit_dialog)

            val text = dialog.findViewById(R.id.tvexitdialog) as TextView

            text.text = msg
        }
        val btnno: Button = dialog?.findViewById(R.id.btnno) as Button
        btnno.setOnClickListener { dialog.dismiss() }
        val btnyes: Button = dialog.findViewById(R.id.btnyes) as Button
        btnyes.setOnClickListener {

            activity.finishAffinity()

        }
        dialog.show()
    }
}