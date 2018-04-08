package com.marijannovak.autismhelper.modules.login.mvvm

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.marijannovak.autismhelper.common.listeners.GeneralListener
import com.marijannovak.autismhelper.database.AppDatabase
import com.marijannovak.autismhelper.models.SignupRequest
import com.marijannovak.autismhelper.models.User
import com.marijannovak.autismhelper.network.APIService
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync

/**
 * Created by Marijan on 23.3.2018..
 */
class LoginRepository : ILoginRepository {

    private var authService : FirebaseAuth = FirebaseAuth.getInstance()
    private var currentUser : FirebaseUser? = null

    override fun checkLoggedIn() : Single<User> {
        return AppDatabase
                .getUserDao()
                .getUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun register(signupRequest: SignupRequest, listener : GeneralListener<FirebaseUser>) {
        authService.createUserWithEmailAndPassword(signupRequest.email, signupRequest.password)
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
                        listener.onFailure(task.exception ?: Exception("Unknown error"))
                    }
                }
    }

    override fun googleSignIn(data: Intent, listener: GeneralListener<FirebaseUser>) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)

            authService.signInWithCredential(credential)
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            listener.onSucces(authService.currentUser!!)
                        } else {
                            listener.onFailure(it.exception ?: Exception("Google Sign In Error"))
                        }
                    }

        } catch (e: ApiException) {
            listener.onFailure(e)
        }

    }

    override fun checkIfUserExists(userId: String): Single<Boolean> {
        return APIService
                .getApi()
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

    override fun saveUserToFirebase(user: User): Completable {
        return APIService
                .getApi()
                .putUser(user.id, user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun saveUser(user: User) {
        doAsync {
            AppDatabase.getUserDao().saveUser(user)
        }
    }

    override fun fetchUserData(userId : String): Single<User> {
        return APIService
                .getApi()
                .getUser(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

}