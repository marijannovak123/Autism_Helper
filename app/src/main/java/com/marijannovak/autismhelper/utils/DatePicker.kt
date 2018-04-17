package com.marijannovak.autismhelper.utils

import android.app.DatePickerDialog
import android.app.DialogFragment
import android.content.Context
import java.util.*


class DatePicker : DialogFragment() {

    companion object {
        @JvmStatic
        fun show(context: Context, listener: DatePickerDialog.OnDateSetListener, date: Calendar?) {

            date?.let {
                DatePickerDialog(context, listener, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH)).show()
                return
            }

            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            return DatePickerDialog(context, listener, year, month, day).show()
        }
    }
}