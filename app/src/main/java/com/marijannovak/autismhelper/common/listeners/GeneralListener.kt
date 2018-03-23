package com.marijannovak.autismhelper.common.listeners

/**
 * Created by Marijan on 23.3.2018..
 */
interface GeneralListener<T> {
    fun onSucces(model : T)
    fun onFailure(t : Throwable)
}