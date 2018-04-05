package com.marijannovak.autismhelper.utils

import android.app.AlertDialog
import android.content.Context
import com.marijannovak.autismhelper.R
import kotlinx.android.synthetic.main.common_dialog.*

class PromptDialog {

    companion object {
        var alertDialog: AlertDialog? = null

        fun show(context: Context, message: String, confirmText: String, cancelText: String, confirmListener: () -> Unit) {
            val alertDialogBuilder = AlertDialog.Builder(context).setView(R.layout.common_dialog)

            alertDialog?.let {
                if(it.isShowing) it.dismiss()
            }

            alertDialog = alertDialogBuilder.create()

            with(alertDialog) {
                this?.let {
                    show()
                    tvDialogMessage.text = message
                    btnPositive.text = confirmText
                    btnNegative.text = cancelText

                    btnPositive.setOnClickListener {
                        dismiss()
                        confirmListener()
                    }

                    btnNegative.setOnClickListener { dismiss() }
                }
            }

        }
    }
}