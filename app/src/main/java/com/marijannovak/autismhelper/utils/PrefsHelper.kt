package com.marijannovak.autismhelper.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.marijannovak.autismhelper.config.Constants.Companion.KEY_DOWNLOADED_IMAGES
import com.marijannovak.autismhelper.config.Constants.Companion.KEY_PARENT_PASSWORD
import com.tumblr.remember.Remember

class PrefsHelper {

    //fun isLoggedIn() = Remember.getBoolean(KEY_LOGGED_IN, false)
//
    //fun setLoggedIn(loggedIn: Boolean) {
    //    Remember.putBoolean(KEY_LOGGED_IN, loggedIn)
    //}

    fun getParentPassword() = Remember.getString(KEY_PARENT_PASSWORD, "")

    fun setParentPassword(password: String) {
        Remember.putString(KEY_PARENT_PASSWORD, password)
    }

    fun setDownloadedImages(imageUrls: List<String>) {
        val type = object : TypeToken<List<String>>() {}.type
        val urlsString = Gson().toJson(imageUrls, type)
        Remember.putString(KEY_DOWNLOADED_IMAGES, urlsString)
    }

    fun getDownloadedImages(): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        val urlsString = Remember.getString(KEY_DOWNLOADED_IMAGES, "")
        return if (urlsString.isEmpty()) emptyList() else Gson().fromJson(urlsString, type)
    }
}