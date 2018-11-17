package com.marijannovak.autismhelper.viewmodels

import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseViewModel
import com.marijannovak.autismhelper.data.models.CategoryQuestionsAnswersJoin
import com.marijannovak.autismhelper.data.models.ChildScore
import com.marijannovak.autismhelper.repositories.DataRepository
import com.marijannovak.autismhelper.repositories.QuizRepository
import com.marijannovak.autismhelper.utils.Resource
import javax.inject.Inject

class QuizViewModel
@Inject constructor(
        private val repository: QuizRepository
): BaseViewModel<CategoryQuestionsAnswersJoin>() {

    fun loadCategoryData(categoryId: Int) {
        compositeDisposable.add(
                repository.getCategoryData(categoryId).subscribe(
                        { category -> resourceLiveData.value = Resource.success(category) },
                        { error -> resourceLiveData.value = Resource.message(R.string.category_load_fail, error.message ?: "") }
                )
        )
    }

    fun saveChildScore(score: ChildScore) {
        resourceLiveData.value = Resource.loading()
        compositeDisposable.add(
                repository.saveScoreLocallyAndOnline(score).subscribe(
                        { resourceLiveData.value = Resource.saved() },
                        { resourceLiveData.value = Resource.message(R.string.save_error, it.message ?: "") }
                )
        )
    }
}