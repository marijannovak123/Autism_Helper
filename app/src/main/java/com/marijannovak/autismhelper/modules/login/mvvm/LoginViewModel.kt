package com.marijannovak.autismhelper.modules.login.mvvm

import android.content.Intent
import com.google.firebase.auth.FirebaseUser
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseViewModel
import com.marijannovak.autismhelper.common.listeners.GeneralListener
import com.marijannovak.autismhelper.data.models.Category
import com.marijannovak.autismhelper.data.models.Question
import com.marijannovak.autismhelper.data.models.SignupRequest
import com.marijannovak.autismhelper.data.models.User
import com.marijannovak.autismhelper.utils.Resource
import com.marijannovak.autismhelper.utils.mapToUser
import io.reactivex.CompletableObserver
import io.reactivex.MaybeObserver
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import org.jetbrains.anko.doAsync
import javax.inject.Inject

/**
 * Created by Marijan on 23.3.2018..
 */
class LoginViewModel @Inject constructor (
        private val repository: LoginRepository) : BaseViewModel<User>() {

    fun checkLoggedIn() {
        repository.isLoggedIn().subscribe(object: MaybeObserver<User> {
            override fun onSuccess(user: User?) {
                user?.let {
                    resourceLiveData.value = Resource.success(listOf(it))
                }
            }

            override fun onComplete() {
                resourceLiveData.value = Resource.home()
            }

            override fun onSubscribe(d: Disposable?) {
                compositeDisposable.add(d)
            }

            override fun onError(e: Throwable?) {
                //NOOP
            }
        })
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
        repository.checkIfUserExists(user.uid).subscribe(object : SingleObserver<Boolean> {
                override fun onSuccess(exists: Boolean?) {
                    exists?.let {
                        if(it)
                            fetchAndSaveUserData(user.uid)
                        else {
                            resourceLiveData.value = Resource.signedUp(listOf(user.mapToUser()))
                        }
                    }
                }

                override fun onSubscribe(d: Disposable?) {
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable?) {
                    if(e is NoSuchElementException) {
                        resourceLiveData.value = Resource.signedUp(listOf(user.mapToUser()))
                    } else {
                        resourceLiveData.value = Resource.message(R.string.unknown_error)
                    }
                }
            }
        )

    }

    private fun fetchAndSaveUserData(userId : String) {
       repository.fetchAndSaveUser(userId).subscribe(object : CompletableObserver {
           override fun onComplete() {
               syncData()
           }

           override fun onSubscribe(d: Disposable?) {
               compositeDisposable.add(d)
           }

           override fun onError(e: Throwable?) {
               resourceLiveData.value = Resource.message(R.string.fetch_user_error)
           }

       })
    }

    fun saveUserOnlineAndLocally(user: User) {
        repository.uploadAndSaveUser(user).subscribe(object : CompletableObserver {
            override fun onComplete() {
                syncData()
            }

            override fun onSubscribe(d: Disposable?) {
                compositeDisposable.add(d)
            }

            override fun onError(e: Throwable?) {
                resourceLiveData.value = Resource.message(R.string.firebase_upload_error)
            }
        })
    }

    fun syncData() {
        dataRepository.syncData().subscribe(object: CompletableObserver{
            override fun onComplete() {
                dataRepository.downloadImages { resourceLiveData.value = Resource.success(null) }
            }

            override fun onSubscribe(d: Disposable?) {
                compositeDisposable.add(d)
            }

            override fun onError(e: Throwable?) {
                resourceLiveData.value = Resource.message(R.string.sync_error)
            }
        })
    }
}