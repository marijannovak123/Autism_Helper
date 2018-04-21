package com.marijannovak.autismhelper.modules.parent.mvvm

import com.marijannovak.autismhelper.common.base.BaseViewModel
import com.marijannovak.autismhelper.data.models.UserChildrenJoin
import com.marijannovak.autismhelper.utils.Resource
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class ParentViewModel @Inject constructor(private val repository: ParentRepository)
    : BaseViewModel<UserChildrenJoin>() {
    fun loadUser() {
        dataRepository.loadUserAndChildren().subscribe(object: SingleObserver<UserChildrenJoin> {
            override fun onSuccess(userWithChildren: UserChildrenJoin?) {
                userWithChildren?.let {
                    resourceLiveData.value = Resource.success(listOf(it))
                }
            }

            override fun onSubscribe(d: Disposable?) {
                compositeDisposable.add(d)
            }

            override fun onError(e: Throwable?) {
                e.let {
                    resourceLiveData.value = Resource.message(it?.message!!)
                }
            }

        })
    }

}