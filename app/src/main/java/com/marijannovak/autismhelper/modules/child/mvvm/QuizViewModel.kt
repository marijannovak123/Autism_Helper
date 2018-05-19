package com.marijannovak.autismhelper.modules.child.mvvm

import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseViewModel
import com.marijannovak.autismhelper.data.models.ChildScore
import com.marijannovak.autismhelper.modules.child.mvvm.repo.QuizRepository
import com.marijannovak.autismhelper.utils.Resource
import javax.inject.Inject

class QuizViewModel @Inject constructor(private val repository: QuizRepository) :
        BaseViewModel<Any>() {

    fun loadCategoryData(categoryId: Int) {
        compositeDisposable.add(
                repository.getCategoryData(categoryId).subscribe(
                        { category -> resourceLiveData.value = Resource.success(listOf(category)) },
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