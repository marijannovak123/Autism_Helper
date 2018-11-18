package com.marijannovak.autismhelper.viewmodels

import androidx.lifecycle.MutableLiveData
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseViewModel
import com.marijannovak.autismhelper.data.models.AacPhrase
import com.marijannovak.autismhelper.data.models.PhraseCategory
import com.marijannovak.autismhelper.data.models.PhrasesSavedSentencesJoin
import com.marijannovak.autismhelper.data.models.SavedSentence
import com.marijannovak.autismhelper.repositories.DataRepository
import com.marijannovak.autismhelper.repositories.AACRepository
import com.marijannovak.autismhelper.utils.Resource
import com.marijannovak.autismhelper.utils.onCompletion
import io.reactivex.rxkotlin.plusAssign
import kotlinx.android.synthetic.main.list_item_child.view.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class AACViewModel
@Inject constructor(
        private val repository: AACRepository
): BaseViewModel<PhrasesSavedSentencesJoin>() {

    var phraseCategoryLiveData = MutableLiveData<List<PhraseCategory>>()
    var phrasesLiveData = MutableLiveData<List<AacPhrase>>()
    var sentenceLivedata = MutableLiveData<List<SavedSentence>>()

    fun loadPhrases(phraseCategoryId: Int) {
        uiScope.launch {
            val categoryPhrasesChannel = repository.getCategoryPhrases(phraseCategoryId)
            for(categoryPhrases in categoryPhrasesChannel) {
                phrasesLiveData.postValue(categoryPhrases)
            }
        }
    }

    fun saveSentence(sentence: SavedSentence) {
        setLoading()
        uiScope.launch {
            repository.saveSentence(sentence)
                    .onCompletion { error ->
                        error?.let {
                            setMessage(R.string.save_error)
                        } ?: setSuccess()
                    }
        }
    }

    fun loadPhraseCategories() {
        uiScope.launch {
            val phraseCategoryChannel = repository.loadPhraseCategories()
            for(phraseCategories in phraseCategoryChannel) {
                phraseCategoryLiveData.postValue(phraseCategories)
            }
        }
    }

    fun loadSentences() {
        uiScope.launch {
            val sentenceChannel = repository.loadSentences()
            for(sentences in sentenceChannel) {
                sentenceLivedata.postValue(sentences)
            }
        }
    }

    fun deleteSentence(sentence: SavedSentence) {
        uiScope.launch {
            repository.deleteSentence(sentence)
                    .onCompletion { error ->
                        error?.let {
                            setMessage(R.string.error)
                        } ?: setMessage(R.string.deleted)
                    }
        }
    }

}