package com.marijannovak.autismhelper.utils

import com.marijannovak.autismhelper.config.Constants.Companion.KEY_LOGGED_IN
import com.tumblr.remember.Remember

class PrefsHelper {

    companion object {

        fun isLoggedIn() = Remember.getBoolean(KEY_LOGGED_IN, false)

        fun setLoggedIn(loggedIn: Boolean) {
            Remember.putBoolean(KEY_LOGGED_IN, loggedIn)
        }
    }
}