package com.marijannovak.autismhelper.modules.login.mvvm

import com.google.firebase.auth.FirebaseUser
import com.marijannovak.autismhelper.common.listeners.GeneralListener

/**
 * Created by Marijan on 23.3.2018..
 */
interface ILoginRepository {
    fun checkLoggedIn(): FirebaseUser?
    fun register(email : String, password : String, listener : GeneralListener<FirebaseUser>)
    fun login(email: String, password: String, listener: GeneralListener<FirebaseUser>)
}