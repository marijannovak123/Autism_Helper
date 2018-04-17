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
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import javax.inject.Inject

/**
 * Created by Marijan on 23.3.2018..
 */
class LoginViewModel @Inject constructor (
        private val repository: LoginRepository) : BaseViewModel<User>() {

    fun checkLoggedIn() {
        if(repository.isLoggedIn()) {
            resourceLiveData.value = Resource.success(null)
        }
    }

    fun register(signupRequest: SignupRequest) {
        resourceLiveData.value = Resource.loading()
        repository.register(signupRequest, object : GeneralListener<FirebaseUser> {
            override fun onSucces(model: FirebaseUser) {
                resourceLiveData.value = Resource.signedUp(listOf(model.mapToUser(signupRequest)))
            }

            override fun onFailure(t: Throwable) {
                resourceLiveData.value = Resource.message(t.message!!)
            }
        })
    }

    fun login(email: String, password: String) {
        resourceLiveData.value = Resource.loading()
        repository.login(email, password, object : GeneralListener<FirebaseUser> {
            override fun onSucces(model: FirebaseUser) {
                fetchUserData(model.uid)
            }

            override fun onFailure(t: Throwable) {
                resourceLiveData.value = Resource.message(t.message!!)
            }
        })
    }

    fun forgotPassword(email: String) {
        repository.forgotPassword(email, object: GeneralListener<Any>{
            override fun onSucces(model: Any) {
                resourceLiveData.value = Resource.message(R.string.success)
            }

            override fun onFailure(t: Throwable) {
                resourceLiveData.value = Resource.message(t.message!!)
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
                resourceLiveData.value = Resource.message(t.message!!)
            }
        })
    }

    private fun checkIfUserAlreadyExists(user: FirebaseUser) {
        repository.checkIfUserExists(user.uid).subscribe(object : SingleObserver<Boolean> {
                override fun onSuccess(exists: Boolean?) {
                    exists?.let {
                        if(it)
                            fetchUserData(user.uid)
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
                        resourceLiveData.value = Resource.success(listOf(user.mapToUser()))
                    } else {
                        resourceLiveData.value = Resource.message(R.string.unknown_error)
                    }
                }
            }
        )

    }

    private fun fetchUserData(userId : String) {
       repository.fetchUserData(userId).subscribe(object : SingleObserver<User>{
           override fun onSuccess(user: User?) {
               user?.let {
                   repository.saveUser(user)
                   repository.setLoggedIn(true)
                   syncData()
                   return
               }
               resourceLiveData.value = Resource.message(R.string.unknown_error)
           }

           override fun onSubscribe(d: Disposable?) {
               compositeDisposable.add(d)
           }

           override fun onError(e: Throwable?) {
               if(e?.message != null) {
                   resourceLiveData.value = Resource.message(e.message!!)
               } else {
                   resourceLiveData.value = Resource.message(R.string.unknown_error)
               }
           }
       })
    }

    fun uploadAndSaveUser(user : User) {
        repository.saveUserToFirebase(user).subscribe(object : CompletableObserver {
            override fun onComplete() {
                repository.saveUser(user)
                repository.setLoggedIn(true)
                syncData()
            }

            override fun onSubscribe(d: Disposable?) {
                compositeDisposable.add(d)
            }

            override fun onError(e: Throwable?) {
                if(e?.message != null) {
                    resourceLiveData.value = Resource.message(e.message!!)
                } else {
                    resourceLiveData.value = Resource.message(R.string.unknown_error)
                }
            }
        })
    }

    fun syncData() {
        dataRepository.syncData().subscribe(object : SingleObserver<Boolean> {
            override fun onSuccess(syncDone: Boolean?) {
                if(syncDone!!) {
                    resourceLiveData.value = Resource.success(null)
                } else {
                    resourceLiveData.value = Resource.message(R.string.unknown_error)
                }
            }

            override fun onSubscribe(d: Disposable?) {
                compositeDisposable.add(d)
            }

            override fun onError(e: Throwable?) {
                dataRepository.deleteDataTables()
                resourceLiveData.value = Resource.message(R.string.unknown_error)
            }
        })
    }
}