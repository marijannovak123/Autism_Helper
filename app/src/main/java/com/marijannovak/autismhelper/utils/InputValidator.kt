package com.marijannovak.autismhelper.utils

import com.marijannovak.autismhelper.config.Constants.Companion.VALIDATION_DATE
import com.marijannovak.autismhelper.config.Constants.Companion.VALIDATION_EMAIL
import com.marijannovak.autismhelper.config.Constants.Companion.VALIDATION_NAME
import com.marijannovak.autismhelper.config.Constants.Companion.VALIDATION_PASSWORD
import com.marijannovak.autismhelper.config.Constants.Companion.VALIDATION_USERNAME
import com.marijannovak.autismhelper.data.models.Child
import java.util.*
import kotlin.collections.HashMap

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

        fun validateChild(child: Child): HashMap<String, String> {
            val errorMap = HashMap<String, String>()

            val dobCalender = Calendar.getInstance()
            dobCalender.time = Date(child.dateOfBirth)

            if(child.name.isEmpty()) {
                errorMap[VALIDATION_NAME] = "Insert name!"
                return errorMap
            }

            if(child.name.length < 2)
                errorMap[VALIDATION_NAME] = "Name too short!"

            if(child.dateOfBirth <= 0)
                errorMap[VALIDATION_DATE] = "Insert date!"

            if(!validateDate(dobCalender))
                errorMap[VALIDATION_DATE] = "Date incorrect!"

            return errorMap
        }


        private fun validateDate(selectedDate: Calendar) = !(selectedDate.before(Date(0)) || selectedDate.after(Calendar.getInstance()))
    }
}