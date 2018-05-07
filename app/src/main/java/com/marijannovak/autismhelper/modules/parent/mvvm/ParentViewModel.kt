package com.marijannovak.autismhelper.modules.parent.mvvm

import android.arch.lifecycle.MutableLiveData
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseViewModel
import com.marijannovak.autismhelper.data.models.Child
import com.marijannovak.autismhelper.data.models.ChildScore
import com.marijannovak.autismhelper.data.models.UserChildrenJoin
import com.marijannovak.autismhelper.utils.Resource
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import org.reactivestreams.Subscriber
import javax.inject.Inject

class ParentViewModel @Inject constructor(private val repository: ParentRepository)
    : BaseViewModel<UserChildrenJoin>() {

    var childScoreLiveData = MutableLiveData<Resource<List<ChildScore>>>()

    fun loadUserWithChildren() {
        compositeDisposable.add(
                dataRepository.loadUserAndChildren().subscribe(
                        { resourceLiveData.value = Resource.success(listOf(it))},
                        { resourceLiveData.value = Resource.message(R.string.load_error)}
                )
        )
    }

    fun saveChild(child: Child) {
        resourceLiveData.value = Resource.loading()
        compositeDisposable.add(
                repository.saveChildLocallyAndOnline(child).subscribe(
                        { resourceLiveData.value = Resource.message(R.string.child_saved) },
                        { resourceLiveData.value = Resource.message(R.string.error_inserting) }
                )

        )
    }

    fun loadChildScores(childId: String) {
        childScoreLiveData.value = Resource.loading()
        compositeDisposable.add(
                repository.loadChildScores(childId).subscribe(
                        { childScoreLiveData.value = Resource.success(it)},
                        { childScoreLiveData.value = Resource.message(R.string.load_error)}
                )
        )
    }

}