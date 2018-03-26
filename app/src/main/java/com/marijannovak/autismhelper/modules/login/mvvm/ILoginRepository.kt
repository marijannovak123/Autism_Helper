package com.marijannovak.autismhelper.modules.login.mvvm

import com.google.firebase.auth.FirebaseUser
import com.marijannovak.autismhelper.common.listeners.GeneralListener
import com.marijannovak.autismhelper.models.User
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

/**
 * Created by Marijan on 23.3.2018..
 */
interface ILoginRepository {
    fun checkLoggedIn(): Maybe<User>
    fun register(email : String, password : String, listener : GeneralListener<FirebaseUser>)
    fun login(email: String, password: String, listener: GeneralListener<FirebaseUser>)
    fun syncUser(user : User) : Completable
    fun saveUser(user : User)
    fun fetchUserData(userId: String): Single<User>
}