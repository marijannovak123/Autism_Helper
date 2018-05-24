package com.marijannovak.autismhelper.modules.login.mvvm

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.marijannovak.autismhelper.common.listeners.GeneralListener
import com.marijannovak.autismhelper.config.Constants
import com.marijannovak.autismhelper.data.database.dao.ChildDao
import com.marijannovak.autismhelper.data.database.dao.ChildScoreDao
import com.marijannovak.autismhelper.data.database.dao.UserDao
import com.marijannovak.autismhelper.data.models.SignupRequest
import com.marijannovak.autismhelper.data.models.User
import com.marijannovak.autismhelper.data.network.API
import com.marijannovak.autismhelper.utils.PrefsHelper
import com.marijannovak.autismhelper.utils.handleThreading
import com.marijannovak.autismhelper.utils.mapToList
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by Marijan on 23.3.2018..
 */
@Singleton
class LoginRepository @Inject constructor(
        private val auth: FirebaseAuth,
        private val userDao: UserDao,
        private val childDao: ChildDao,
        private val childScoreDao: ChildScoreDao,
        @Named(Constants.API_JSON)
        private val api: API,
        private val prefs: PrefsHelper) {

    private var currentUser: FirebaseUser? = null

    fun isLoggedIn(): Maybe<User> {
        return userDao.userLoggedIn().handleThreading()
    }

    fun register(signupRequest: SignupRequest, listener: GeneralListener<FirebaseUser>) {
        auth.createUserWithEmailAndPassword(signupRequest.email, signupRequest.password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        currentUser = auth.currentUser
                        listener.onSucces(currentUser!!)
                    } else {
                        val exception = task.exception ?: Exception("Unknown message")
                        listener.onFailure(Throwable(exception.message))
                    }
                }
    }

    fun login(email: String, password: String, listener: GeneralListener<FirebaseUser>) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
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
                        if (it.isSuccessful) {
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
                    if (user.id.isNotEmpty() && user.username != null
                            && user.username!!.isNotEmpty() && user.email != null
                            && user.email!!.isNotEmpty())
                        Single.just(true)
                    else Single.just(false)
                }
                .handleThreading()
    }

    fun uploadAndSaveUser(user: User): Completable {
        return Completable.mergeArray(
                api.putUser(user.id, user),
                saveUser(user)
        ).handleThreading()
    }

    private fun saveUser(user: User): Completable {
        return Completable.fromAction {
            userDao.insert(user)
            user.children?.let {
                childDao.insertMultiple(it.mapToList())
            }
            user.childScores?.let {
                childScoreDao.insertMultiple(it.mapToList())
            }
            prefs.setParentPassword(user.parentPassword ?: "")


        }
    }

    fun fetchAndSaveUser(userId: String): Completable {
        return api.getUser(userId)
                .flatMapCompletable { user ->
                    saveUser(user)
                }
                .handleThreading()
    }

}
