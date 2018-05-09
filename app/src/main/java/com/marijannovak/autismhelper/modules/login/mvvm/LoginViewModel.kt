package com.marijannovak.autismhelper.modules.login.mvvm

import android.content.Intent
import com.google.firebase.auth.FirebaseUser
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseViewModel
import com.marijannovak.autismhelper.common.listeners.GeneralListener
import com.marijannovak.autismhelper.data.models.SignupRequest
import com.marijannovak.autismhelper.data.models.User
import com.marijannovak.autismhelper.utils.Resource
import com.marijannovak.autismhelper.utils.mapToUser
import javax.inject.Inject

/**
 * Created by Marijan on 23.3.2018..
 */
class LoginViewModel @Inject constructor (
        private val repository: LoginRepository) : BaseViewModel<User>() {

    fun checkLoggedIn() {
        compositeDisposable.add(
                repository.isLoggedIn().subscribe(
                        { resourceLiveData.value = Resource.success(listOf(it))},
                        { /*NOOP don't show error, just show content*/ },
                        { resourceLiveData.value = Resource.home() }
                )
        )
    }

    fun register(signupRequest: SignupRequest) {
        resourceLiveData.value = Resource.loading()
        repository.register(signupRequest, object : GeneralListener<FirebaseUser> {
            override fun onSucces(model: FirebaseUser) {
                resourceLiveData.value = Resource.signedUp(listOf(model.mapToUser(signupRequest)))
            }

            override fun onFailure(t: Throwable) {
                resourceLiveData.value = Resource.message(R.string.register_error)
            }
        })
    }

    fun login(email: String, password: String) {
        resourceLiveData.value = Resource.loading()
        repository.login(email, password, object : GeneralListener<FirebaseUser> {
            override fun onSucces(model: FirebaseUser) {
                fetchAndSaveUserData(model.uid)
            }

            override fun onFailure(t: Throwable) {
                resourceLiveData.value = Resource.message(R.string.login_error)
            }
        })
    }

    fun forgotPassword(email: String) {
        repository.forgotPassword(email, object: GeneralListener<Any>{
            override fun onSucces(model: Any) {
                resourceLiveData.value = Resource.message(R.string.success)
            }

            override fun onFailure(t: Throwable) {
                resourceLiveData.value = Resource.message(R.string.unknown_error)
            }
        })
    }

    fun googleSignIn(data: Intent) {
        resourceLiveData.value = Resource.loading()
        repository.googleSignIn(data, object : GeneralListener<FirebaseUser> {
            override fun onSucces(model: FirebaseUser) {
                checkIfUserAlreadyExists(model)
            }

            override fun onFailure(t: Throwable) {
                resourceLiveData.value = Resource.message(R.string.google_sign_in_error)
            }
        })
    }

    private fun checkIfUserAlreadyExists(user: FirebaseUser) {
        compositeDisposable.add(
                repository.checkIfUserExists(user.uid).subscribe(
                        {
                            if(it) {
                                fetchAndSaveUserData(user.uid)
                            } else {
                                resourceLiveData.value = Resource.signedUp(listOf(user.mapToUser()))
                            }
                        },
                        {
                            if(it is NoSuchElementException) {
                                resourceLiveData.value = Resource.signedUp(listOf(user.mapToUser()))
                            } else {
                                throwErrorAndLogOut(R.string.unknown_error)
                            }
                        }
                )
        )
    }

    private fun fetchAndSaveUserData(userId : String) {
        compositeDisposable.add(
                repository.fetchAndSaveUser(userId).subscribe(
                        { syncData() },
                        { throwErrorAndLogOut(R.string.fetch_user_error)}
                )
        )

    }

    fun saveUserOnlineAndLocally(user: User) {
        resourceLiveData.value = Resource.loading()
        compositeDisposable.add(
                repository.uploadAndSaveUser(user).subscribe(
                        { syncData() },
                        { throwErrorAndLogOut(R.string.firebase_upload_error) }
                )
        )
    }

    fun syncData() {
        compositeDisposable.add(
                dataRepository.syncData().subscribe(
                        { dataRepository.downloadImages { resourceLiveData.value = Resource.success(null) }},
                        { throwErrorAndLogOut(R.string.sync_error) }
                )
        )
    }

    fun throwErrorAndLogOut(msgRes: Int) {
        resourceLiveData.value = Resource.message(msgRes)
        logOut()
    }
}