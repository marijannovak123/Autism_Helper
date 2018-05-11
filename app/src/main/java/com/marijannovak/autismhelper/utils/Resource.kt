package com.marijannovak.autismhelper.utils

import com.marijannovak.autismhelper.App
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.enums.Status

class Resource<out T>(val status: Status, val data: T?, val message: String?) {

    override fun toString(): String {
        return "Resource{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}'
    }

    companion object {

        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> message(msgRes: Int): Resource<T> {
            return Resource(Status.MESSAGE, null, App.getAppContext().resources.getString(msgRes))
        }

        fun <T> loading(): Resource<T> {
            return Resource(Status.LOADING, null, null)
        }

        fun <T> syncing(): Resource<T> {
            return Resource(Status.SYNCING, null, App.getAppContext().resources.getString(R.string.syncing))
        }

        fun <T> progressUpdate(msgRes: Int): Resource<T> {
            return Resource(Status.PROGRESS_UPDATE, null, App.getAppContext().resources.getString(msgRes))
        }

        fun <T> next(): Resource<T>? {
            return Resource(Status.NEXT, null, null)
        }

        fun <T> home(): Resource<T> {
            return Resource(Status.HOME, null, null)
        }

        fun <T> signedUp(data: T?): Resource<T> {
            return Resource(Status.SIGNEDUP, data, null)
        }

        fun <T> saved(saveable: T): Resource<T> {
            return Resource(Status.SAVED, saveable, null)
        }
    }
}