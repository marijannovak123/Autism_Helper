package com.marijannovak.autismhelper.modules.child.mvvm

import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseViewModel
import com.marijannovak.autismhelper.data.models.AacPhrase
import com.marijannovak.autismhelper.data.models.SavedSentence
import com.marijannovak.autismhelper.data.repo.DataRepository
import com.marijannovak.autismhelper.modules.child.mvvm.repo.AACRepository
import com.marijannovak.autismhelper.utils.Resource
import javax.inject.Inject

class AACViewModel
@Inject constructor(
        private val repository: AACRepository,
        dataRepository: DataRepository)
    : BaseViewModel<AacPhrase>(dataRepository) {

    //todo: load saved sentences
    fun loadPhrases() {
        compositeDisposable.add(
                repository.getPhrases().subscribe(
                        { phrases -> resourceLiveData.value = Resource.success(phrases) },
                        { error -> resourceLiveData.value = Resource.message(R.string.load_error, error.message ?: "") }
                )
        )
    }

    fun saveSentence(sentence: SavedSentence) {
        compositeDisposable.add(
                repository.saveSentence(sentence).subscribe({
                    resourceLiveData.value = Resource.success(null)
                }, {
                    resourceLiveData.value = Resource.message(R.string.save_error, it.message ?: "")
                })
        )
    }

}