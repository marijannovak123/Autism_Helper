package com.marijannovak.autismhelper.modules.login.mvvm

import com.google.firebase.auth.FirebaseUser
import com.marijannovak.autismhelper.common.base.BaseViewModel
import com.marijannovak.autismhelper.common.enums.Enums.State
import com.marijannovak.autismhelper.common.listeners.GeneralListener
import com.marijannovak.autismhelper.models.User
import com.marijannovak.autismhelper.sync.ISyncRepository
import io.reactivex.CompletableObserver
import io.reactivex.MaybeObserver
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
                .subscribeWith(object : MaybeObserver<User> {
                    override fun onSuccess(user: User?) {
                       user?.let {
                           contentLiveData.value = listOf(it)
                           stateLiveData.value = State.CONTENT
                       }
                    }

                    override fun onComplete() {
                        //NOOP
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

    fun register(email : String, password : String) {
        stateLiveData.value = State.LOADING
        repository.register(email, password, object : GeneralListener<FirebaseUser> {
            override fun onSucces(model: FirebaseUser) {
                //todo: more data from form
                saveUserToFirebase(User(model.displayName, model.uid, model.email))
            }

            override fun onFailure(t: Throwable) {
                stateLiveData.value = State.ERROR
                errorLiveData.value = t
            }
        })
    }

    fun login(email: String, password: String) {
        stateLiveData.value = State.LOADING
        repository.login(email, password, object : GeneralListener<FirebaseUser>{
            override fun onSucces(model: FirebaseUser) {
                fetchUserData(model.uid)
            }

            override fun onFailure(t: Throwable) {
                stateLiveData.value = State.ERROR
                errorLiveData.value = t
            }
        })
    }

    private fun fetchUserData(userId : String) {
       repository.fetchUserData(userId).subscribeWith(object : SingleObserver<User>{
           override fun onSuccess(user: User?) {
               if(user != null) {
                   repository.saveUser(user)
                   stateLiveData.value = State.CONTENT
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
        repository.saveUserToFirebase(user).subscribeWith(object : CompletableObserver {
            override fun onComplete() {
                repository.saveUser(user)
                stateLiveData.value = State.CONTENT
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
        syncRepository.syncData(object : SingleObserver<Boolean> {
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