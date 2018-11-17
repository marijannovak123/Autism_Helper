package com.marijannovak.autismhelper.viewmodels

import android.content.Intent
import com.google.firebase.auth.FirebaseUser
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseViewModel
import com.marijannovak.autismhelper.common.enums.Status
import com.marijannovak.autismhelper.common.listeners.GeneralListener
import com.marijannovak.autismhelper.data.models.SignupRequest
import com.marijannovak.autismhelper.data.models.User
import com.marijannovak.autismhelper.repositories.LoginRepository
import com.marijannovak.autismhelper.utils.*
import kotlinx.android.synthetic.main.list_item_child.view.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Marijan on 23.3.2018..
 */
class LoginViewModel @Inject constructor(
        private val repository: LoginRepository
): BaseViewModel<User>() {

    fun checkLoggedIn() {
        setLoading()
        uiScope.launch {
            repository.isLoggedIn()
                    .onSuccess { user ->
                        user?.let {
                            syncUserData(it)
                        } ?: setState(Status.HOME)
                    }.onError { setMessage(R.string.data_load_error) }
        }
    }

    private fun syncUserData(user: User) {
        setLoading()
        uiScope.launch {
            dataRepository.syncUserData()
        }.invokeOnCompletion { error ->
            error?.let {
                setMessage(R.string.data_load_error)
            } ?: setData(user)
        }
    }

    fun register(signupRequest: SignupRequest) {
        setLoading()
        repository.register(signupRequest, object : GeneralListener<FirebaseUser> {
            override fun onSucces(model: FirebaseUser) {
                resourceLiveData.value = Resource.signedUp(model.mapToUser(signupRequest))
            }

            override fun onFailure(t: Throwable) {
                setMessage(R.string.register_error)
            }
        })
    }

    fun login(email: String, password: String) {
        setLoading()
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
        setLoading()
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
        uiScope.launch {
            repository.checkIfUserExists(user.uid)
                    .onSuccess {
                        fetchAndSaveUserData(user.uid)
                    }.onError {
                        if(it is NoSuchElementException)
                        else throwErrorAndLogOut(R.string.error)
                    }
        }
    }

    private fun fetchAndSaveUserData(userId: String) {
        uiScope.launch {
            repository.fetchAndSaveUser(userId)
        }.invokeOnCompletion { error ->
            error?.let {
                throwErrorAndLogOut(R.string.fetch_error)
            } ?: syncContent()
        }
    }

    fun saveUserOnlineAndLocally(user: User) {
        setLoading()
        uiScope.launch {
            repository.uploadAndSaveUser(user)
        }.invokeOnCompletion { error ->
            error?.let {
                throwErrorAndLogOut(R.string.firebase_upload_error)
            } ?: syncContent()
        }
    }

    private fun syncContent() {
        setLoading(R.string.downloading_resources)
        uiScope.launch {
            dataRepository.syncData(true)
        }.invokeOnCompletion { error ->
            error?.let {
                throwErrorAndLogOut(R.string.sync_error)
            } ?: downloadImages()
        }
    }

    private fun downloadImages() {
        dataRepository.downloadImages({ setSuccess()}, { throwErrorAndLogOut(R.string.sync_error) })
    }

    private fun throwErrorAndLogOut(msgRes: Int) {
        setMessage(msgRes)
        logOut()
    }
}