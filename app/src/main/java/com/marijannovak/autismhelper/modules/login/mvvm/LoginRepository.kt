package com.marijannovak.autismhelper.modules.login.mvvm

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.marijannovak.autismhelper.common.listeners.GeneralListener
import com.marijannovak.autismhelper.models.User
import com.marijannovak.autismhelper.network.APIService
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Marijan on 23.3.2018..
 */
//todo: google sign in
//todo: post user to db to save his data
class LoginRepository : ILoginRepository {
    private var authService : FirebaseAuth
    private var currentUser : FirebaseUser? = null

    init {
        authService = FirebaseAuth.getInstance()
    }

    override fun checkLoggedIn() : FirebaseUser? {
        currentUser = authService.currentUser
        currentUser?.let {
            return it
        }
        return null
    }

    override fun register(email: String, password: String, listener : GeneralListener<FirebaseUser>) {
        authService.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        currentUser = authService.currentUser
                        listener.onSucces(currentUser!!)
                    } else {
                        val exception = task.exception ?: Exception("Unknown error")
                        listener.onFailure(Throwable(exception.message))
                    }
                }
    }

    override fun login(email: String, password: String, listener: GeneralListener<FirebaseUser>) {
        authService.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        currentUser = authService.currentUser
                        listener.onSucces(currentUser!!)
                    } else {
                        val exception = task.exception ?: Exception("Unknown error")
                        listener.onFailure(Throwable(exception.message))
                    }
                }
    }

    override fun syncUser(user: User): Completable {
        return APIService
                .getApi()
                .syncUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}