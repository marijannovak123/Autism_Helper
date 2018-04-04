package com.marijannovak.autismhelper.utils

import com.marijannovak.autismhelper.config.Constants.Companion.VALIDATION_DATE
import com.marijannovak.autismhelper.config.Constants.Companion.VALIDATION_EMAIL
import com.marijannovak.autismhelper.config.Constants.Companion.VALIDATION_PASSWORD
import com.marijannovak.autismhelper.config.Constants.Companion.VALIDATION_USERNAME
import java.util.*

class InputValidator {

    companion object {
        fun validate(input: String, type: String) : Boolean {
            var valid = true

            if (input.isEmpty()) return false

            when(type) {
                VALIDATION_USERNAME -> {
                    if(input.length < 5) valid = false
                }
                VALIDATION_EMAIL -> {
                    if(!android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches())
                        valid = false
                }
                VALIDATION_PASSWORD -> {
                    if(input.length < 8) valid = false
                }
                VALIDATION_DATE -> {
                    if(input.toLong() * 1000 < 0 || input.toLong()*1000 > System.currentTimeMillis())
                        valid = false
                }
            }

            return valid
        }

        fun validateDate(selectedDate: Date?): Boolean {

            if(selectedDate == null) return false

            var valid = true

            if(selectedDate.before(Date(0)) || selectedDate.after(Date(System.currentTimeMillis())))
                valid = false

            return valid
        }

    }
}