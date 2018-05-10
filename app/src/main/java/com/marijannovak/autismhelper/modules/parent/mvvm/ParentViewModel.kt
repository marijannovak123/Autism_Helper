package com.marijannovak.autismhelper.modules.parent.mvvm

import android.arch.lifecycle.MutableLiveData
import com.github.mikephil.charting.data.LineData
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseViewModel
import com.marijannovak.autismhelper.data.models.AacPhrase
import com.marijannovak.autismhelper.data.models.Child
import com.marijannovak.autismhelper.data.models.UserChildrenJoin
import com.marijannovak.autismhelper.data.repo.DataRepository
import com.marijannovak.autismhelper.modules.child.mvvm.repo.AACRepository
import com.marijannovak.autismhelper.utils.Resource
import javax.inject.Inject

class ParentViewModel @Inject constructor(
        private val repository: ParentRepository,
        private val aacRepository: AACRepository)
    : BaseViewModel<UserChildrenJoin>() {

    var chartLiveData = MutableLiveData<Resource<List<ParentRepository.ChartData>>>()
    var phraseLiveData = MutableLiveData<Resource<List<AacPhrase>>>()

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

    fun loadPhrases() {
        phraseLiveData.value = Resource.loading()
        compositeDisposable.add(
                repository.loadPhrases().subscribe(
                        { phraseLiveData.value = Resource.success(it)},
                        { phraseLiveData.value = Resource.message(R.string.load_error)}
                )
        )
    }


    fun savePhrase(phrase: AacPhrase) {
        phraseLiveData.value = Resource.loading()
        compositeDisposable.add(
                aacRepository.savePhrase(phrase).subscribe(
                        { phraseLiveData.value = Resource.saved(listOf(phrase)) },
                        { phraseLiveData.value = Resource.message(R.string.error_saving_phrase) }
                )
        )
    }

    fun syncData() {
        resourceLiveData.value = Resource.loading()
        dataRepository.syncData().subscribe(
                {resourceLiveData.value = Resource.message(R.string.data_synced)},
                {resourceLiveData.value = Resource.message(R.string.sync_error)}
        )
    }
}