package com.marijannovak.autismhelper.modules.login.mvvm

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.marijannovak.autismhelper.common.base.BaseViewModel
import com.marijannovak.autismhelper.common.enums.Enums
import com.marijannovak.autismhelper.common.listeners.GeneralListener
import com.marijannovak.autismhelper.models.User
import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable

/**
 * Created by Marijan on 23.3.2018..
 */
class LoginViewModel(private val repository: ILoginRepository, private val syncRepository : ISyncRepository) : BaseViewModel<FirebaseUser>() {

    val loginRegisterListener : GeneralListener<FirebaseUser>

    init {
        loginRegisterListener = object : GeneralListener<FirebaseUser>{
            override fun onSucces(model: FirebaseUser) {
                stateLiveData.value = Enums.State.CONTENT
                contentLiveData.value = model
            }

            override fun onFailure(t: Throwable) {
                stateLiveData.value = Enums.State.ERROR
                errorLiveData.value = t
            }
        }
    }

    fun checkLogin() {
        val user = repository.checkLoggedIn()
        user?.let { contentLiveData.value = it }
    }

    fun register(email : String, password : String) {
        stateLiveData.value = Enums.State.LOADING
        repository.register(email, password, loginRegisterListener)
    }

    fun login(email: String, password: String) {
        repository.login(email, password, loginRegisterListener)
    }

    //todo: when logging in, post user to db..check if exists first, dl users and check, then post/patch
    fun syncUser(user : User) {
        repository.syncUser(user).subscribeWith(object : CompletableObserver{
            override fun onComplete() {
                Log.e("Test", "complete")
            }

            override fun onSubscribe(d: Disposable?) {
                compositeDisposable.add(d)
            }

            override fun onError(e: Throwable?) {
                e?.let { Log.e("Test", e.message) }
            }
        })
    }

}