package com.marijannovak.autismhelper.utils

import android.app.DatePickerDialog
import android.app.DialogFragment
import android.content.Context
import java.util.*


class DatePicker : DialogFragment() {

    companion object {
        @JvmStatic
        fun newInstance(context: Context, listener: DatePickerDialog.OnDateSetListener) : DatePickerDialog {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            return DatePickerDialog(context, listener, year, month, day)
        }
    }
}