package com.marijannovak.autismhelper.utils

import com.marijannovak.autismhelper.App
import com.marijannovak.autismhelper.R

class ErrorHelper {
    companion object {
        fun unknownError() = Throwable(App.getAppContext().getString(R.string.unknown_error))
    }
}