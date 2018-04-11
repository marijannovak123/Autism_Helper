package com.marijannovak.autismhelper.modules.login.mvvm

import android.content.Intent
import com.google.firebase.auth.FirebaseUser
import com.marijannovak.autismhelper.common.base.BaseViewModel
import com.marijannovak.autismhelper.common.enums.Enums.State
import com.marijannovak.autismhelper.common.listeners.GeneralListener
import com.marijannovak.autismhelper.models.SignupRequest
import com.marijannovak.autismhelper.models.User
import com.marijannovak.autismhelper.common.repo.IDataRepository
import com.marijannovak.autismhelper.utils.mapToUser
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
/**
 * Created by Marijan on 23.3.2018..
 */
class LoginViewModel(private val repository: ILoginRepository,
                     private val dataRepository : IDataRepository)
    : BaseViewModel<User>() {

    fun checkLoggedIn() {
        if(repository.isLoggedIn()) {
            stateLiveData.value = State.NEXT
        } else {
            stateLiveData.value = State.CONTENT
        }
    }

    fun register(signupRequest: SignupRequest) {
        stateLiveData.value = State.LOADING
        repository.register(signupRequest, object : GeneralListener<FirebaseUser> {
            override fun onSucces(model: FirebaseUser) {
                contentLiveData.value = listOf(model.mapToUser(signupRequest))
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

    fun forgotPassword(email: String) {
        repository.forgotPassword(email, object: GeneralListener<Any>{
            override fun onSucces(model: Any) {
                stateLiveData.value = State.SUCCESS
            }

            override fun onFailure(t: Throwable) {
                errorLiveData.value = t
            }
        })
    }

    fun googleSignIn(data: Intent) {
        stateLiveData.value = State.LOADING
        repository.googleSignIn(data, object : GeneralListener<FirebaseUser> {
            override fun onSucces(model: FirebaseUser) {
                checkIfUserAlreadyExists(model)
            }

            override fun onFailure(t: Throwable) {
                stateLiveData.value = State.ERROR
                errorLiveData.value = t
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
                            contentLiveData.value = listOf(user.mapToUser())
                        }
                    }
                }

                override fun onSubscribe(d: Disposable?) {
                    compositeDisposable.add(d)
                }

                override fun onError(e: Throwable?) {

                    if(e is NoSuchElementException) {
                        contentLiveData.value = listOf(user.mapToUser())
                    } else {
                        stateLiveData.value = State.ERROR
                        errorLiveData.value = e
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
                   stateLiveData.value = State.SYNC
                   return
               }

               stateLiveData.value = State.ERROR
               errorLiveData.value = Throwable("Null response")
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

    fun uploadAndSaveUser(user : User) {
        repository.saveUserToFirebase(user).subscribe(object : CompletableObserver {
            override fun onComplete() {
                repository.saveUser(user)
                repository.setLoggedIn(true)
                stateLiveData.value = State.SYNC
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
        dataRepository.syncData().subscribe(object : SingleObserver<Boolean> {
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
                dataRepository.deleteDataTables()
                stateLiveData.value = State.ERROR
                errorLiveData.value = e ?: unknownError()
            }
        })
    }

    fun unknownError() = Throwable("Unknown error")

}