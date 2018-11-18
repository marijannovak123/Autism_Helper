package com.marijannovak.autismhelper.viewmodels

import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseViewModel
import com.marijannovak.autismhelper.common.enums.Status
import com.marijannovak.autismhelper.data.models.CategoryQuestionsAnswersJoin
import com.marijannovak.autismhelper.data.models.ChildScore
import com.marijannovak.autismhelper.repositories.DataRepository
import com.marijannovak.autismhelper.repositories.QuizRepository
import com.marijannovak.autismhelper.utils.Resource
import com.marijannovak.autismhelper.utils.onCompletion
import kotlinx.android.synthetic.main.list_item_child.view.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class QuizViewModel @Inject constructor(
        private val repository: QuizRepository
): BaseViewModel<CategoryQuestionsAnswersJoin>() {

    fun loadCategoryData(categoryId: Int) {
        uiScope.launch {
            val categoriesChannel = repository.getCategoryData(categoryId)
            for(categories in categoriesChannel) {
                setData(categories)
            }
        }
    }

    fun saveChildScore(score: ChildScore) {
        setLoading()
        uiScope.launch {
            repository.saveScoreLocallyAndOnline(score)
                    .onCompletion { error ->
                        error?.let {
                            setMessage(R.string.save_error)
                        } ?: setState(Status.SAVED)
                    }
        }
    }
}