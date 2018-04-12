package com.marijannovak.autismhelper.modules.login.mvvm

import android.content.Intent
import com.google.firebase.auth.FirebaseUser
import com.marijannovak.autismhelper.common.listeners.GeneralListener
import com.marijannovak.autismhelper.data.models.SignupRequest
import com.marijannovak.autismhelper.data.models.User
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created by Marijan on 23.3.2018..
 */
interface ILoginRepository {
    fun register(signupRequest: SignupRequest, listener: GeneralListener<FirebaseUser>)
    fun login(email: String, password: String, listener: GeneralListener<FirebaseUser>)
    fun saveUserToFirebase(user : User) : Completable
    fun saveUser(user : User)
    fun fetchUserData(userId: String): Single<User>
    fun googleSignIn(data: Intent, listener: GeneralListener<FirebaseUser>)
    fun checkIfUserExists(userId: String): Single<Boolean>
    fun forgotPassword(email: String, listener: GeneralListener<Any>)
    fun isLoggedIn() : Boolean
    fun setLoggedIn(loggedIn: Boolean)
}