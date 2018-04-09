package com.marijannovak.autismhelper.utils

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.config.Constants.Companion.CHILD_ID_SUFFIX
import com.marijannovak.autismhelper.config.Constants.Companion.VALIDATION_DATE
import com.marijannovak.autismhelper.config.Constants.Companion.VALIDATION_NAME
import com.marijannovak.autismhelper.models.Child
import com.marijannovak.autismhelper.models.User
import org.jetbrains.anko.alert
import org.jetbrains.anko.selector
import java.util.*
import kotlin.collections.HashMap

class DialogHelper {

    companion object {

        fun showPromptDialog(context: Context, message: String, confirmListener: () -> Unit) {
            context.alert(message){
                positiveButton(R.string.confirm) { confirmListener() }
                negativeButton(R.string.cancel) {  }
            }.show()
        }


        fun showSelector(context: Context, title: String, stringList: List<String>, confirmListener: (Int) -> Unit) {
            context.selector(title, stringList,
                    { _, i  -> confirmListener(i)})
        }
//todo: test correct data set
        @SuppressLint("InflateParams")
        fun showAddChildDialog(context: Context, user: User, confirmListener: (Child) -> Unit) {
            val selectedDate : Calendar = Calendar.getInstance()

            val builder = AlertDialog.Builder(context, R.style.CustomAlertDialog)
            val inflater = LayoutInflater.from(context)
            val alertView = inflater.inflate(R.layout.dialog_add_child, null)
            builder.setView(alertView)
            builder.setCancelable(false)
            val alertDialog = builder.create()

            val message = alertView.findViewById<TextView>(R.id.tvMessage)
            val btnPositive = alertView.findViewById<TextView>(R.id.btnPositive)
            val btnNegative = alertView.findViewById<TextView>(R.id.btnNegative)
            val etName = alertView.findViewById<EditText>(R.id.etChildName)
            val etDateOfBirth = alertView.findViewById<EditText>(R.id.etChildDateOfBirth)
            val spSex = alertView.findViewById<Spinner>(R.id.spSex)

            spSex.adapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, listOf("M", "F"))
            message.text = context.getString(R.string.add_child_profile)
            etDateOfBirth.setOnClickListener { showDatePicker(context, selectedDate, etDateOfBirth) }

            btnPositive.text = context.getString(R.string.confirm)
            btnPositive.setOnClickListener {
                val name = etName.text.toString().trim()
                val dateOfBirth = selectedDate.timeInMillis

                val childId = user.id + CHILD_ID_SUFFIX + user.children.size
                val child = Child(childId, name, spSex.selectedItem.toString(), user.id, dateOfBirth)

                val errors = InputValidator.validateChild(child)

                if(errors.isEmpty()) {
                    confirmListener(child)
                    alertDialog.dismiss()
                } else {
                    handleChildAddErrors(errors, etName, etDateOfBirth)
                }
            }

            btnNegative.text = context.getString(R.string.cancel)
            btnNegative.setOnClickListener { alertDialog.dismiss() }

            alertDialog.show()
        }

        private fun handleChildAddErrors(errors: HashMap<String, String>, etName: EditText, etDateOfBirth: EditText) {
            if(errors.isEmpty())  {
                etName.error = null
                etDateOfBirth.error = null
            } else {
                for(e in errors.iterator())
                    when (e.key){
                        VALIDATION_NAME -> etName.error = e.value
                        VALIDATION_DATE -> etDateOfBirth.error = e.value
                    }
            }
        }

        @SuppressLint("SetTextI18n")
        private fun showDatePicker(context: Context, date: Calendar, etDate: EditText) {

            val onDateSet = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                date.set(Calendar.YEAR, year)
                date.set(Calendar.MONTH, month)
                date.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                etDate.setText("${date[Calendar.DAY_OF_MONTH]}.${date[Calendar.MONTH]+1}.${date[Calendar.YEAR]}")
            }

            DatePicker.newInstance(context, onDateSet, date).show()
        }

    }
}
