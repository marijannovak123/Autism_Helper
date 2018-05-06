package com.marijannovak.autismhelper.modules.parent.mvvm

import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseViewModel
import com.marijannovak.autismhelper.data.models.Child
import com.marijannovak.autismhelper.data.models.UserChildrenJoin
import com.marijannovak.autismhelper.utils.Resource
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import org.reactivestreams.Subscriber
import javax.inject.Inject

class ParentViewModel @Inject constructor(private val repository: ParentRepository)
    : BaseViewModel<UserChildrenJoin>() {

    fun loadUserWithChildren() {
        compositeDisposable.add(
                dataRepository.loadUserAndChildren().subscribe{
                    resourceLiveData.value = Resource.success(listOf(it))
                }
        )

        //dataRepository.loadUserAndChildren().subscribe(object: SingleObserver<UserChildrenJoin> {
        //    override fun onSuccess(userWithChildren: UserChildrenJoin?) {
        //        userWithChildren?.let {
        //            resourceLiveData.value = Resource.success(listOf(it))
        //        }
        //    }
//
        //    override fun onSubscribe(d: Disposable?) {
        //        compositeDisposable.add(d)
        //    }
//
        //    override fun onError(e: Throwable?) {
        //        resourceLiveData.value = Resource.message(R.string.children_data_load_error)
        //    }
//
        //})
    }

    fun saveChild(child: Child) {
        resourceLiveData.value = Resource.loading()
        repository.saveChildLocallyAndOnline(child).subscribe(object: CompletableObserver {
            override fun onComplete() {
                resourceLiveData.value = Resource.message(R.string.child_saved)
            }

            override fun onSubscribe(d: Disposable?) {
                compositeDisposable.add(d)
            }

            override fun onError(e: Throwable?) {
                resourceLiveData.value = Resource.message(R.string.error_inserting)
            }
        })
    }

}