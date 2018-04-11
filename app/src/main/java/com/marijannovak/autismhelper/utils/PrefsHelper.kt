package com.marijannovak.autismhelper.utils

import com.marijannovak.autismhelper.config.Constants.Companion.KEY_LOGGED_IN
import com.marijannovak.autismhelper.config.Constants.Companion.KEY_PARENT_PASSWORD
import com.tumblr.remember.Remember

class PrefsHelper {

    companion object {

        fun isLoggedIn() = Remember.getBoolean(KEY_LOGGED_IN, false)

        fun setLoggedIn(loggedIn: Boolean) {
            Remember.putBoolean(KEY_LOGGED_IN, loggedIn)
        }

        fun getParentPassword() = Remember.getString(KEY_PARENT_PASSWORD, "")

        fun setParentPassword(password: String) {
            Remember.putString(KEY_PARENT_PASSWORD, password)
        }
    }
}