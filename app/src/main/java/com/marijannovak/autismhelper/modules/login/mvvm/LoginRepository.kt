package com.marijannovak.autismhelper.modules.login.mvvm

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.marijannovak.autismhelper.common.listeners.GeneralListener
import com.marijannovak.autismhelper.data.database.dao.UserDao
import com.marijannovak.autismhelper.data.models.SignupRequest
import com.marijannovak.autismhelper.data.models.User
import com.marijannovak.autismhelper.data.network.API
import com.marijannovak.autismhelper.utils.PrefsHelper
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import javax.inject.Inject

/**
 * Created by Marijan on 23.3.2018..
 */
class LoginRepository @Inject constructor(
        private val auth: FirebaseAuth,
        private val sharedPrefs: PrefsHelper,
        private val userDao: UserDao,
        private val api: API) {
    
    private var currentUser : FirebaseUser? = null

    fun isLoggedIn() = sharedPrefs.isLoggedIn()

    fun setLoggedIn(loggedIn: Boolean) {
        sharedPrefs.setLoggedIn(loggedIn)
    }

    fun register(signupRequest: SignupRequest, listener : GeneralListener<FirebaseUser>) {
        auth.createUserWithEmailAndPassword(signupRequest.email, signupRequest.password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        currentUser = auth.currentUser
                        listener.onSucces(currentUser!!)
                    } else {
                        val exception = task.exception ?: Exception("Unknown message")
                        listener.onFailure(Throwable(exception.message))
                    }
                }
    }

    fun login(email: String, password: String, listener: GeneralListener<FirebaseUser>) {
        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        currentUser = auth.currentUser
                        listener.onSucces(currentUser!!)
                    } else {
                        listener.onFailure(task.exception ?: Exception("Unknown message"))
                    }
                }
    }

    fun forgotPassword(email: String, listener: GeneralListener<Any>) {
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        listener.onSucces(Any())
                    } else {
                        listener.onFailure(task.exception ?: Exception("Unknown message"))
                    }
                }
    }

    fun googleSignIn(data: Intent, listener: GeneralListener<FirebaseUser>) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)

            auth.signInWithCredential(credential)
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            listener.onSucces(auth.currentUser!!)
                        } else {
                            listener.onFailure(it.exception ?: Exception("Google Sign In Error"))
                        }
                    }

        } catch (e: ApiException) {
            listener.onFailure(e)
        }

    }

    fun checkIfUserExists(userId: String): Single<Boolean> {
        return api
                .getUser(userId)
                .flatMap { user: User ->
                    if(user.id.isNotEmpty() && user.username != null
                            && user.username!!.isNotEmpty() && user.email != null
                            && user.email!!.isNotEmpty())
                        Single.just(true)
                    else Single.just(false)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun saveUserToFirebase(user: User): Completable {
        return api
                .putUser(user.id, user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun saveUser(user: User) {
        doAsync {
            userDao.insert(user)
        }
    }

    fun fetchUserData(userId : String): Single<User> {
        return api
                .getUser(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

}