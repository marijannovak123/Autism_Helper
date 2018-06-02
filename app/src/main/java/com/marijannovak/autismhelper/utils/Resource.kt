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

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }

        val resource = other as Resource<*>?

        if (status !== resource!!.status) {
            return false
        }
        if (if (message != null) message != resource!!.message else resource!!.message != null) {
            return false
        }
        return if (data != null) data == resource.data else resource.data == null
    }

    override fun hashCode(): Int {
        var result = status.hashCode()
        result = 31 * result + (message?.hashCode() ?: 0)
        result = 31 * result + (data?.hashCode() ?: 0)
        return result
    }

    companion object {

        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> message(msgRes: Int, throwableMessage: String): Resource<T> {
            return Resource(Status.MESSAGE, null, App.getAppContext().resources.getString(msgRes) + " " + throwableMessage)
        }

        fun <T> loading(): Resource<T> {
            return Resource(Status.LOADING, null, null)
        }

        fun <T> loading(msgRes: Int): Resource<T> {
            return Resource(Status.LOADING, null, App.getAppContext().getString(msgRes))
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

        fun <T> saved(): Resource<T> {
            return Resource(Status.SAVED, null, null)
        }
    }
}