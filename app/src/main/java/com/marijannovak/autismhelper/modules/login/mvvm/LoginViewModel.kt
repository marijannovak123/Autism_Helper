package com.marijannovak.autismhelper.modules.login.mvvm

import android.content.Intent
import com.google.firebase.auth.FirebaseUser
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseViewModel
import com.marijannovak.autismhelper.common.listeners.GeneralListener
import com.marijannovak.autismhelper.data.models.SignupRequest
import com.marijannovak.autismhelper.data.models.User
import com.marijannovak.autismhelper.data.repo.DataRepository
import com.marijannovak.autismhelper.utils.Resource
import com.marijannovak.autismhelper.utils.mapToUser
import javax.inject.Inject

/**
 * Created by Marijan on 23.3.2018..
 */
class LoginViewModel @Inject constructor(
        private val repository: LoginRepository,
        private val dataRepository: DataRepository) : BaseViewModel<User>(dataRepository) {

    fun checkLoggedIn() {
        resourceLiveData.value = Resource.loading()
        compositeDisposable.add(
                repository.isLoggedIn().subscribe(
                        {//sync user data on every first start
                            syncUserData(listOf(it))
                        },
                        { resourceLiveData.value = Resource.message(R.string.data_load_error, it.message ?: "")},
                        { resourceLiveData.value = Resource.home() }
                )
        )
    }

    private fun syncUserData(users: List<User>) {
         compositeDisposable.add(
                 dataRepository.syncUserData().subscribe(
                        //go further regardless sync was successful
                        { resourceLiveData.value = Resource.success(users)},
                        { resourceLiveData.value = Resource.success(users)}
        ))
    }

    fun register(signupRequest: SignupRequest) {
        resourceLiveData.value = Resource.loading()
        repository.register(signupRequest, object : GeneralListener<FirebaseUser> {
            override fun onSucces(model: FirebaseUser) {
                resourceLiveData.value = Resource.signedUp(listOf(model.mapToUser(signupRequest)))
            }

            override fun onFailure(t: Throwable) {
                resourceLiveData.value = Resource.message(R.string.register_error, t.message!!)
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
                resourceLiveData.value = Resource.message(R.string.login_error, t.message!!)
            }
        })
    }

    fun forgotPassword(email: String) {
        repository.forgotPassword(email, object : GeneralListener<Any> {
            override fun onSucces(model: Any) {
                resourceLiveData.value = Resource.message(R.string.success, "")
            }

            override fun onFailure(t: Throwable) {
                resourceLiveData.value = Resource.message(R.string.error, t.message ?: "")
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
                resourceLiveData.value = Resource.message(R.string.google_sign_in_error, t.message ?: "")
            }
        })
    }

    private fun checkIfUserAlreadyExists(user: FirebaseUser) {
        compositeDisposable.add(
                repository.checkIfUserExists(user.uid).subscribe(
                        {
                            if (it) {
                                fetchAndSaveUserData(user.uid)
                            } else {
                                resourceLiveData.value = Resource.signedUp(listOf(user.mapToUser()))
                            }
                        },
                        {
                            if (it is NoSuchElementException) {
                                resourceLiveData.value = Resource.signedUp(listOf(user.mapToUser()))
                            } else {
                                throwErrorAndLogOut(R.string.error, it.message ?: "")
                            }
                        }
                )
        )
    }

    private fun fetchAndSaveUserData(userId: String) {
        compositeDisposable.add(
                repository.fetchAndSaveUser(userId).subscribe(
                        { syncData() },
                        { error -> throwErrorAndLogOut(R.string.fetch_error, error.message ?: "") }
                )
        )

    }

    fun saveUserOnlineAndLocally(user: User) {
        resourceLiveData.value = Resource.loading()
        compositeDisposable.add(
                repository.uploadAndSaveUser(user).subscribe(
                        { syncData() },
                        { throwErrorAndLogOut(R.string.firebase_upload_error, it.message ?: "") }
                )
        )
    }

    private fun syncData() {
        resourceLiveData.value = Resource.loading(R.string.downloading_resources)
        compositeDisposable.add(
                dataRepository.syncData(true).subscribe(
                        {
                            dataRepository.downloadImages({
                                    resourceLiveData.value = Resource.success(null) },
                                    {
                                        throwErrorAndLogOut(R.string.sync_error, it.message ?: "")
                                    })

                         },
                        { throwErrorAndLogOut(R.string.sync_error, it.message ?: "") }
                )
        )
    }

    private fun throwErrorAndLogOut(msgRes: Int, throwableMessage: String) {
        resourceLiveData.value = Resource.message(msgRes, throwableMessage)
        logOut()
    }
}