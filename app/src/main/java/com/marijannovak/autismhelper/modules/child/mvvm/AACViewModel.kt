package com.marijannovak.autismhelper.modules.child.mvvm

import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseViewModel
import com.marijannovak.autismhelper.data.models.AacPhrase
import com.marijannovak.autismhelper.modules.child.mvvm.repo.AACRepository
import com.marijannovak.autismhelper.utils.Resource
import javax.inject.Inject

class AACViewModel @Inject constructor(private val repository: AACRepository)
    : BaseViewModel<AacPhrase>() {

    fun loadPhrases() {
        compositeDisposable.add(
                repository.getPhrases().subscribe(
                        { phrases -> resourceLiveData.value = Resource.success(phrases) },
                        { error -> resourceLiveData.value = Resource.message(R.string.load_error, error.message ?: "") }
                )
        )
    }

}