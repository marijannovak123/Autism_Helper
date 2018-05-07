package com.marijannovak.autismhelper.modules.child.mvvm

import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseViewModel
import com.marijannovak.autismhelper.data.models.CategoryQuestionsAnswersJoin
import com.marijannovak.autismhelper.data.models.ChildScore
import com.marijannovak.autismhelper.utils.Resource
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class QuizViewModel @Inject constructor(private val repository: QuizRepository):
        BaseViewModel<Any>() {

    fun loadCategoryData(categoryId: Int) {
        compositeDisposable.add(
                repository.getCategoryData(categoryId).subscribe(
                        {category -> resourceLiveData.value = Resource.success(listOf(category))},
                        {error -> resourceLiveData.value = Resource.message(R.string.category_load_fail)}
                )
        )
    }

    fun saveChildScore(score: ChildScore) {
        resourceLiveData.value = Resource.loading()
        compositeDisposable.add(
                repository.saveScoreLocallyAndOnline(score).subscribe(
                        { resourceLiveData.value = Resource.saved(listOf(score)) },
                        { resourceLiveData.value = Resource.message(R.string.save_error) }
                )
        )
    }
}