package com.marijannovak.autismhelper.modules.login.mvvm

import com.google.firebase.auth.FirebaseUser
import com.marijannovak.autismhelper.common.base.BaseViewModel
import com.marijannovak.autismhelper.common.enums.Enums
import com.marijannovak.autismhelper.common.listeners.GeneralListener

/**
 * Created by Marijan on 23.3.2018..
 */
class LoginViewModel(private val repository: ILoginRepository) : BaseViewModel<FirebaseUser>() {

    fun checkLogin() {
        val user = repository.checkLoggedIn()
        user?.let { contentLiveData.value = it }
    }

    fun login(email : String, password : String) {
        stateLiveData.value = Enums.State.LOADING

        repository.login(email, password, object : GeneralListener<FirebaseUser> {
            override fun onFailure(t: Throwable) {
                stateLiveData.value = Enums.State.ERROR
                errorLiveData.value = t
            }

            override fun onSucces(model: FirebaseUser) {
                stateLiveData.value = Enums.State.CONTENT
                contentLiveData.value = model
            }
        })
    }

}