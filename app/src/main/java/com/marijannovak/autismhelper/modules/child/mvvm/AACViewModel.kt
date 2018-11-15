package com.marijannovak.autismhelper.modules.child.mvvm

import androidx.lifecycle.MutableLiveData
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseViewModel
import com.marijannovak.autismhelper.data.models.AacPhrase
import com.marijannovak.autismhelper.data.models.PhraseCategory
import com.marijannovak.autismhelper.data.models.PhrasesSavedSentencesJoin
import com.marijannovak.autismhelper.data.models.SavedSentence
import com.marijannovak.autismhelper.data.repo.DataRepository
import com.marijannovak.autismhelper.modules.child.mvvm.repo.AACRepository
import com.marijannovak.autismhelper.utils.Resource
import io.reactivex.rxkotlin.plusAssign
import javax.inject.Inject

class AACViewModel
@Inject constructor(
        private val repository: AACRepository,
        dataRepository: DataRepository)
    : BaseViewModel<PhrasesSavedSentencesJoin>(dataRepository) {

    var phraseCategoryLiveData = MutableLiveData<List<PhraseCategory>>()
    var phrasesLiveData = MutableLiveData<List<AacPhrase>>()
    var sentenceLivedata = MutableLiveData<List<SavedSentence>>()

    fun loadPhrases(phraseCategoryId: Int) {
        compositeDisposable.add(
                repository.getCategoryPhrases(phraseCategoryId).subscribe(
                        { phrases -> phrasesLiveData.value = phrases },
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

    fun loadPhraseCategories() {
        compositeDisposable +=
                repository.loadPhraseCategories()
                        .subscribe({
                            phraseCategoryLiveData.value  = it
                        }, {
                            resourceLiveData.value = Resource.message(R.string.load_error)
                        })
    }

    fun loadSentences() {
        compositeDisposable +=
                repository.loadSentences()
                        .subscribe( {
                            sentenceLivedata.value = it
                        }, {
                            resourceLiveData.value = Resource.message(R.string.load_error)
                        })
    }

    fun deleteSentence(sentence: SavedSentence) {
        compositeDisposable +=
                repository.deleteSentence(sentence)
                        .subscribe({
                            resourceLiveData.value = Resource.message(R.string.deleted)
                        }, {
                            resourceLiveData.value = Resource.message(R.string.error)
                        })
    }

}