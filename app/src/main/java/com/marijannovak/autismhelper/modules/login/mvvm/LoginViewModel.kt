package com.marijannovak.autismhelper.modules.login.mvvm

import android.content.Intent
import com.google.firebase.auth.FirebaseUser
import com.marijannovak.autismhelper.common.base.BaseViewModel
import com.marijannovak.autismhelper.common.enums.Enums.State
import com.marijannovak.autismhelper.common.listeners.GeneralListener
import com.marijannovak.autismhelper.models.SignupRequest
import com.marijannovak.autismhelper.models.User
import com.marijannovak.autismhelper.sync.ISyncRepository
import com.marijannovak.autismhelper.utils.mapToUser
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable

/**
 * Created by Marijan on 23.3.2018..
 */
class LoginViewModel(private val repository: ILoginRepository,
                     private val syncRepository : ISyncRepository)
    : BaseViewModel<User>() {

    fun checkLogin() {
        repository.checkLoggedIn()
                .subscribe(object : SingleObserver<User> {
                    override fun onSuccess(user: User?) {
                       user?.let {
                           contentLiveData.value = listOf(it)
                           stateLiveData.value = State.NEXT
                           return
                       }
                        stateLiveData.value = State.CONTENT
                    }

                    override fun onSubscribe(d: Disposable?) {
                        compositeDisposable.add(d)
                    }

                    override fun onError(e: Throwable?) {
                        stateLiveData.value = State.ERROR
                    }
                })
    }


    fun register(signupRequest: SignupRequest) {
        stateLiveData.value = State.LOADING
        repository.register(signupRequest, object : GeneralListener<FirebaseUser> {
            override fun onSucces(model: FirebaseUser) {
                saveUserToFirebase(model.mapToUser(signupRequest))
            }

            override fun onFailure(t: Throwable) {
                stateLiveData.value = State.ERROR
                errorLiveData.value = t
            }
        })
    }

    fun login(email: String, password: String) {
        stateLiveData.value = State.LOADING
        repository.login(email, password, object : GeneralListener<FirebaseUser> {
            override fun onSucces(model: FirebaseUser) {
                fetchUserData(model.uid)
            }

            override fun onFailure(t: Throwable) {
                stateLiveData.value = State.ERROR
                errorLiveData.value = t
            }
        })
    }

    fun googleSignIn(data: Intent) {
        stateLiveData.value = State.LOADING
        repository.googleSignIn(data, object : GeneralListener<FirebaseUser> {
            override fun onSucces(model: FirebaseUser) {
                if(checkIfUserAlreadyExists(model.uid))
                    fetchUserData(model.uid)
                else {
                    saveUserToFirebase(model.mapToUser())
                }
            }

            override fun onFailure(t: Throwable) {
                stateLiveData.value = State.ERROR
                errorLiveData.value = t
            }
        })
    }

    private fun checkIfUserAlreadyExists(userId: String): Boolean {
        var isSuccess = false
        repository.checkIfUserExists(userId).subscribe(object : SingleObserver<Boolean> {
                override fun onSuccess(exists: Boolean?) {
                    exists?.let { isSuccess = it }
                }

                override fun onSubscribe(d: Disposable?) {
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable?) {
                    //noop
                }
            }
        )

        return isSuccess
    }

    private fun fetchUserData(userId : String) {
       repository.fetchUserData(userId).subscribe(object : SingleObserver<User>{
           override fun onSuccess(user: User?) {
               if(user != null) {
                   repository.saveUser(user)
                   contentLiveData.value = listOf(user)
               } else {
                   stateLiveData.value = State.ERROR
                   errorLiveData.value = Throwable("Null response")
               }
           }

           override fun onSubscribe(d: Disposable?) {
               compositeDisposable.add(d)
           }

           override fun onError(e: Throwable?) {
               stateLiveData.value = State.ERROR
               errorLiveData.value = e ?: unknownError()
           }
       })
    }

    fun saveUserToFirebase(user : User) {
        repository.saveUserToFirebase(user).subscribe(object : CompletableObserver {
            override fun onComplete() {
                repository.saveUser(user)
                contentLiveData.value = listOf(user)
            }

            override fun onSubscribe(d: Disposable?) {
                compositeDisposable.add(d)
            }

            override fun onError(e: Throwable?) {
                stateLiveData.value = State.ERROR
                errorLiveData.value = e ?: unknownError()
            }
        })
    }

    fun syncData() {
        syncRepository.syncData().subscribe(object : SingleObserver<Boolean> {
            override fun onSuccess(syncDone: Boolean?) {
                if(syncDone!!) {
                    stateLiveData.value = State.NEXT
                } else {
                    stateLiveData.value = State.ERROR
                    errorLiveData.value = unknownError()
                }
            }

            override fun onSubscribe(d: Disposable?) {
                compositeDisposable.add(d)
            }

            override fun onError(e: Throwable?) {
                syncRepository.deleteDataTables()
                stateLiveData.value = State.ERROR
                errorLiveData.value = e ?: unknownError()
            }
        })
    }

    fun unknownError() = Throwable("Unknown error")

}