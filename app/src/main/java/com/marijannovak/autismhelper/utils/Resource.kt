package com.marijannovak.autismhelper.utils

import com.marijannovak.autismhelper.App
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

        fun <T> message(msg: String): Resource<T> {
            return Resource(Status.MESSAGE, null, msg)
        }

        fun <T> loading(): Resource<T> {
            return Resource(Status.LOADING, null, null)
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
    }
}