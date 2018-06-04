package com.marijannovak.autismhelper.modules.login.mvvm

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.marijannovak.autismhelper.common.listeners.GeneralListener
import com.marijannovak.autismhelper.config.Constants
import com.marijannovak.autismhelper.data.database.AppDatabase
import com.marijannovak.autismhelper.data.models.SignupRequest
import com.marijannovak.autismhelper.data.models.User
import com.marijannovak.autismhelper.data.network.API
import com.marijannovak.autismhelper.utils.PrefsHelper
import com.marijannovak.autismhelper.utils.mapToList
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Scheduler
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
        private val db: AppDatabase,
        private val api: API,
        private val prefs: PrefsHelper,
        @Named(Constants.SCHEDULER_IO) private val ioScheduler: Scheduler,
        @Named(Constants.SCHEDULER_MAIN) private val mainScheduler: Scheduler) {

    private var currentUser: FirebaseUser? = null

    fun isLoggedIn(): Maybe<User> {
        return db.userDao().userLoggedIn().subscribeOn(ioScheduler).observeOn(mainScheduler)
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
                .subscribeOn(ioScheduler).observeOn(mainScheduler)
    }

    fun uploadAndSaveUser(user: User): Completable {
        return Completable.mergeArray(
                api.putUser(user.id, user),
                saveUser(user)
        ).subscribeOn(ioScheduler).observeOn(mainScheduler)
    }

    private fun saveUser(user: User): Completable {
        return Completable.fromAction {
            db.userDao().insert(user)
            user.children?.let {
                db.childDao().insertMultiple(it.mapToList())
            }
            user.childScores?.let {
                db.childScoreDao().insertMultiple(it.mapToList())
            }
            prefs.setParentPassword(user.parentPassword ?: "")
        }
    }

    fun fetchAndSaveUser(userId: String): Completable {
        return api.getUser(userId)
                .flatMapCompletable { user ->
                    saveUser(user)
                }
                .subscribeOn(ioScheduler).observeOn(mainScheduler)
    }

}
