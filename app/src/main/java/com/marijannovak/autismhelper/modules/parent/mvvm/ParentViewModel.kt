package com.marijannovak.autismhelper.modules.parent.mvvm

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseViewModel
import com.marijannovak.autismhelper.data.models.*
import com.marijannovak.autismhelper.modules.child.mvvm.repo.AACRepository
import com.marijannovak.autismhelper.utils.Resource
import com.marijannovak.autismhelper.utils.logTag
import javax.inject.Inject

class ParentViewModel @Inject constructor(
        private val repository: ParentRepository,
        private val aacRepository: AACRepository)
    : BaseViewModel<UserChildrenJoin>() {

    var userName = ""
    val userNameLiveData = MutableLiveData<String>()
    val chartLiveData = MutableLiveData<ParentRepository.ChartData>()
    val phraseLiveData = MutableLiveData<List<AacPhrase>>()
    val childrenLiveData = MutableLiveData<List<Child>>()
    val userLiveData = MutableLiveData<User>()

    fun loadUsername() {
        compositeDisposable.add(
                repository.loadUserName().subscribe(
                        {
                            userName = it
                            userNameLiveData.value = it
                            resourceLiveData.value = Resource.success(null)
                        },
                        {
                            Log.e(logTag(), it.message)
                            resourceLiveData.value = Resource.message(R.string.load_error)
                        }
                )
        )
    }

    fun loadChildren() {
        resourceLiveData.value = Resource.loading()
        compositeDisposable.add(
                repository.loadChildren().subscribe(
                        {
                            childrenLiveData.value = it
                            resourceLiveData.value = Resource.success(null)
                        },
                        { resourceLiveData.value = Resource.message(R.string.load_error) }
                )
        )
    }

    fun saveChild(child: Child) {
        resourceLiveData.value = Resource.loading()
        compositeDisposable.add(
                repository.saveChildLocallyAndOnline(child).subscribe(
                        { resourceLiveData.value = Resource.message(R.string.child_saved) },
                        { it -> resourceLiveData.value = Resource.message(R.string.error_inserting) }
                )
        )
    }

    fun loadChildScores(child: Child) {
        compositeDisposable.add(
                repository.loadChildScoresLineData(child).subscribe(
                        {
                            chartLiveData.value = it
                            resourceLiveData.value = Resource.success(null)
                        },
                        { resourceLiveData.value = Resource.message(R.string.load_error) }
                )
        )
    }

    fun loadPhrases() {
        resourceLiveData.value = Resource.loading()
        compositeDisposable.add(
                repository.loadPhrases().subscribe(
                        {
                            phraseLiveData.value = it
                            resourceLiveData.value = Resource.success(null)
                        },
                        { resourceLiveData.value = Resource.message(R.string.load_error) }
                )
        )
    }


    fun savePhrase(phrase: AacPhrase) {
        resourceLiveData.value = Resource.loading()
        compositeDisposable.add(
                aacRepository.savePhrase(phrase).subscribe(
                        { resourceLiveData.value = Resource.message(R.string.phrase_saved) },
                        { resourceLiveData.value = Resource.message(R.string.error_saving_phrase) }
                )
        )
    }

    fun syncData(firstSync: Boolean) {
        resourceLiveData.value = Resource.loading()
        dataRepository.syncData(firstSync).subscribe(
                {
                    dataRepository.downloadImages {
                        resourceLiveData.value = Resource.message(R.string.data_synced)
                    }
                },
                { resourceLiveData.value = Resource.message(R.string.sync_error) }
        )
    }

    fun deletePhrase(phrase: AacPhrase) {
        resourceLiveData.value = Resource.loading()
        aacRepository.deletePhrase(phrase).subscribe(
                { resourceLiveData.value = Resource.message(R.string.saved) },
                { resourceLiveData.value = Resource.message(R.string.save_error) }
        )
    }

    fun updateUserData(userId: String, userUpdateRequest: UserUpdateRequest) {
        resourceLiveData.value = Resource.loading()
        repository.updateUser(userId, userUpdateRequest).subscribe({
            resourceLiveData.value = Resource.message(R.string.saved)
        }, {
            resourceLiveData.value = Resource.message(R.string.save_error)
        })
    }

    fun loadUser() {
        resourceLiveData.value = Resource.loading()
        compositeDisposable.add(
                repository.loadUser().subscribe(
                        {
                            userLiveData.value = it
                            resourceLiveData.value = Resource.success(null)
                        },
                        { resourceLiveData.value = Resource.message(R.string.fetch_user_error) }
                )
        )
    }

}