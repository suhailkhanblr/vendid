package com.bylancer.classified.bylancerclassified.widgets

import android.graphics.drawable.ColorDrawable
import android.R
import android.app.Dialog
import android.content.Context
import android.view.Window
import android.view.Window.FEATURE_NO_TITLE



class BylancerProgressBar {

    companion object {
        var mDialog = null

        fun showDialog(context: Context) {
            var dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            //dialog.setContentView(R.layout.dialog_login)
            dialog.getWindow().setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
           // mDialog = dialog
        }
    }
}