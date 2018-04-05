package com.marijannovak.autismhelper.utils

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
            }

            return valid
        }

        fun validateDate(selectedDate:Calendar): Boolean {

            var valid = true

            if(selectedDate.before(Date(0)) || selectedDate.after(Calendar.getInstance()))
                valid = false

            return valid
        }

    }
}