package com.marijannovak.autismhelper.utils

import android.app.AlertDialog
import android.content.Context
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.models.Child
import kotlinx.android.synthetic.main.common_dialog.*
import org.jetbrains.anko.*
import org.jetbrains.anko.design.textInputLayout

class DialogHelper {

    companion object {
        private var alertDialog: AlertDialog? = null

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

        fun promptDialog(context: Context, message: String, confirmListener: () -> Unit) {
           context.alert(message){
               positiveButton("Confirm") { confirmListener() }
               negativeButton("Cancel") {  }
           }.show()
        }

        fun addChildDialog(context: Context, confirmListener: (Child) -> Unit) {
            context.alert("Add a child to your profile"){

                var etName : EditText? = null
                var etDateOfBirth : EditText? = null
                var spSex : Spinner? = null

                val spinnerAdapter = ArrayAdapter(context,android.R.layout.simple_list_item_1, listOf("M", "F"))

                customView{

                    verticalLayout {

                        textInputLayout {
                            etName = editText {
                                hint = context.getString(R.string.child_name)
                            }
                        }.lparams(matchParent, wrapContent) {
                            horizontalMargin = dip(40)
                            verticalMargin = dip(10)
                            topMargin = dip(20)
                        }

                            textInputLayout {
                                etDateOfBirth = editText {
                                    hint = context.getString(R.string.date_of_birth)
                                }
                            }.lparams(matchParent, wrapContent) {
                                horizontalMargin = dip(40)
                                verticalMargin = dip(10)
                            }

                            spSex = spinner {
                                adapter = spinnerAdapter
                            }.lparams(matchParent, wrapContent) {
                                horizontalMargin = dip(40)
                                verticalMargin = dip(10)
                            }
                        }
                    }

                positiveButton("Confirm") {
                    //todo: validation
                    confirmListener(Child(12121, etName!!.text.toString(), spSex!!.selectedItem.toString(), "kita", 213213213))
                }
                negativeButton("Cancel") {  }
            }.show()
        }

        fun showSelector(context: Context) {
            context.selector("Select something", listOf("Hehe", "Haha"),
                    { _, i  -> context.toast(i.toString())})
        }
    }
}