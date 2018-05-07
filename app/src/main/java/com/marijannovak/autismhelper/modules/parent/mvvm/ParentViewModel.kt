package com.marijannovak.autismhelper.modules.parent.mvvm

import android.arch.lifecycle.MutableLiveData
import com.github.mikephil.charting.data.LineData
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseViewModel
import com.marijannovak.autismhelper.data.models.Child
import com.marijannovak.autismhelper.data.models.UserChildrenJoin
import com.marijannovak.autismhelper.utils.Resource
import javax.inject.Inject

class ParentViewModel @Inject constructor(private val repository: ParentRepository)
    : BaseViewModel<UserChildrenJoin>() {

    var chartLiveData = MutableLiveData<Resource<List<LineData>>>()

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
        chartLiveData.value = Resource.loading()
        compositeDisposable.add(
                repository.loadChildScoresLineData(childId).subscribe(
                        { chartLiveData.value = Resource.success(listOf(it))},
                        { chartLiveData.value = Resource.message(R.string.load_error)}
                )
        )
    }

}