package com.marijannovak.autismhelper.utils

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.support.v7.widget.AppCompatButton
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.config.Constants.Companion.CHILD_ID_SUFFIX
import com.marijannovak.autismhelper.config.Constants.Companion.GENDERS
import com.marijannovak.autismhelper.config.Constants.Companion.VALIDATION_DATE
import com.marijannovak.autismhelper.config.Constants.Companion.VALIDATION_EMAIL
import com.marijannovak.autismhelper.config.Constants.Companion.VALIDATION_NAME
import com.marijannovak.autismhelper.data.models.Child
import org.jetbrains.anko.alert
import java.util.*

class DialogHelper {

    companion object {

        fun showPromptDialog(context: Context, message: String, confirmListener: () -> Unit) {
            context.alert(message) {
                positiveButton(R.string.confirm) { confirmListener() }
                negativeButton(R.string.cancel) { }
            }.show()
        }

        @SuppressLint("SetTextI18n")
        fun showDatePicker(context: Context, date: Calendar, etDate: EditText) {
            val onDateSet = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                date.set(Calendar.YEAR, year)
                date.set(Calendar.MONTH, month)
                date.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                etDate.setText("${date[Calendar.DAY_OF_MONTH]}.${date[Calendar.MONTH] + 1}.${date[Calendar.YEAR]}")
            }
            DatePicker.show(context, onDateSet, date)
        }

        @SuppressLint("InflateParams")
        fun showForgotPasswordDialog(context: Context, confirmListener: (String) -> Unit) {
            val builder = AlertDialog.Builder(context, R.style.CustomAlertDialog)
            val inflater = LayoutInflater.from(context)
            val alertView = inflater.inflate(R.layout.dialog_forgot_password, null)

            builder.setView(alertView)
            builder.setCancelable(false)
            val alertDialog = builder.create()

            val btnPositive = alertView.findViewById<AppCompatButton>(R.id.btnPositive)
            val btnNegative = alertView.findViewById<AppCompatButton>(R.id.btnNegative)
            val etEmail = alertView.findViewById<EditText>(R.id.etEmail)

            btnPositive.setOnClickListener {
                val email = etEmail.text.toString().trim()

                if (InputValidator.validate(email, VALIDATION_EMAIL)) {
                    confirmListener(email)
                    alertDialog.dismiss()
                } else {
                    etEmail.error = context.getString(R.string.malformed_email)
                }
            }

            btnNegative.setOnClickListener {
                alertDialog.dismiss()
            }

            alertDialog.show()
        }

        @SuppressLint("InflateParams")
        fun showEnterParentPasswordDialog(context: Context, parentPassword: String, confirmListener: (String) -> Unit) {
            val builder = AlertDialog.Builder(context, R.style.CustomAlertDialog)
            val inflater = LayoutInflater.from(context)
            val alertView = inflater.inflate(R.layout.dialog_enter_password, null)

            builder.setView(alertView)
            builder.setCancelable(false)
            val alertDialog = builder.create()

            val btnPositive = alertView.findViewById<AppCompatButton>(R.id.btnPositive)
            val btnNegative = alertView.findViewById<AppCompatButton>(R.id.btnNegative)
            val etPassword = alertView.findViewById<EditText>(R.id.etParentPassword)

            if (parentPassword == "") {
                val message = alertView.findViewById<TextView>(R.id.tvMessage)
                message.text = context.getText(R.string.choose_parent_password)
            }

            btnPositive.setOnClickListener {
                val password = etPassword.text.toString().trim()
                confirmListener(password)
                alertDialog.dismiss()
            }

            btnNegative.setOnClickListener {
                alertDialog.dismiss()
            }

            alertDialog.show()
        }

        //todo: test correct data set
        @SuppressLint("InflateParams")
        fun showAddChildDialog(context: Context, userId: String, userChildrenNo: Int, showAnotherCheck: Boolean, confirmListener: (Child, Boolean) -> Unit, cancel: () -> Unit) {
            val selectedDate: Calendar = Calendar.getInstance()

            val builder = AlertDialog.Builder(context, R.style.CustomAlertDialog)
            val inflater = LayoutInflater.from(context)
            val alertView = inflater.inflate(R.layout.dialog_add_child, null)

            builder.setView(alertView)
            builder.setCancelable(false)
            val alertDialog = builder.create()

            val btnPositive = alertView.findViewById<AppCompatButton>(R.id.btnPositive)
            val btnNegative = alertView.findViewById<AppCompatButton>(R.id.btnNegative)

            if (userChildrenNo == 0) {
                btnNegative.visibility = View.INVISIBLE
                btnNegative.isEnabled = false
            }

            val etName = alertView.findViewById<EditText>(R.id.etChildName)
            val etDateOfBirth = alertView.findViewById<EditText>(R.id.etChildDateOfBirth)
            val spGender = alertView.findViewById<Spinner>(R.id.spGender)
            val cbAddAnother = alertView.findViewById<CheckBox>(R.id.cbAddAnother)

            cbAddAnother.visibility = if (!showAnotherCheck) View.GONE else View.VISIBLE
            spGender.adapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, GENDERS)

            etDateOfBirth.setOnClickListener { showDatePicker(context, selectedDate, etDateOfBirth) }

            btnPositive.setOnClickListener {
                val name = etName.text.toString().trim()
                val dateOfBirth = selectedDate.timeInMillis

                val childId = userId + CHILD_ID_SUFFIX + userChildrenNo
                val child = Child(childId, userId, name, spGender.selectedItem.toString(), dateOfBirth)

                val errors = InputValidator.validateChild(child)

                if (errors.isEmpty()) {
                    confirmListener(child, cbAddAnother.isChecked && showAnotherCheck)
                    alertDialog.dismiss()
                } else {
                    handleChildAddErrors(errors, etName, etDateOfBirth)
                }
            }

            btnNegative.setOnClickListener {
                cancel()
                alertDialog.dismiss()
            }

            alertDialog.show()
        }

        private fun handleChildAddErrors(errors: HashMap<String, String>, etName: EditText, etDateOfBirth: EditText) {
            if (errors.isEmpty()) {
                etName.error = null
                etDateOfBirth.error = null
            } else {
                for (e in errors.iterator())
                    when (e.key) {
                        VALIDATION_NAME -> etName.error = e.value
                        VALIDATION_DATE -> etDateOfBirth.error = e.value
                    }
            }
        }

        fun showPickChildDialog(context: Context, children: List<Child>, onConfirm: (Child) -> Unit) {
            val builder = AlertDialog.Builder(context, R.style.CustomAlertDialog)
            val inflater = LayoutInflater.from(context)
            val alertView = inflater.inflate(R.layout.dialog_pick_child, null)

            builder.setView(alertView)
            builder.setCancelable(false)
            val alertDialog = builder.create()

            val btnPositive = alertView.findViewById<AppCompatButton>(R.id.btnPositive)
            val btnNegative = alertView.findViewById<AppCompatButton>(R.id.btnNegative)
            val spChildren = alertView.findViewById<Spinner>(R.id.spChildren)

            var childrenNames = emptyList<String>()

            for (child: Child in children) {
                childrenNames += child.name
            }

            val childrenAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, childrenNames)
            spChildren.adapter = childrenAdapter

            btnPositive.setOnClickListener {
                onConfirm(children[spChildren.selectedItemPosition])
                alertDialog.dismiss()
            }

            btnNegative.setOnClickListener {
                alertDialog.dismiss()
            }

            alertDialog.show()
        }

    }
}
