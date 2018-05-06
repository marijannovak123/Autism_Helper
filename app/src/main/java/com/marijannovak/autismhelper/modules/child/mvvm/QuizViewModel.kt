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
        BaseViewModel<CategoryQuestionsAnswersJoin>() {

    fun loadCategoryData(categoryId: Int) {
        repository.getCategoryData(categoryId).subscribe(object: SingleObserver<CategoryQuestionsAnswersJoin> {
            override fun onSuccess(category: CategoryQuestionsAnswersJoin?) {
                category?.let {
                    resourceLiveData.value = Resource.success(listOf(it))
                }
            }

            override fun onSubscribe(d: Disposable?) {
                compositeDisposable.add(d)
            }

            override fun onError(e: Throwable?) {
                resourceLiveData.value = Resource.message(R.string.category_load_fail)
            }

        })
    }

    fun saveChildScore(score: ChildScore) {
        resourceLiveData.value = Resource.loading()
        val pair = repository.saveScoreToDb(score)
        val completable = pair.first
        val scoreToSave = pair.second
        completable.subscribe(object: CompletableObserver {
            override fun onComplete() {
                uploadScoreToFirebase(scoreToSave)
            }

            override fun onSubscribe(d: Disposable?) {
                compositeDisposable.add(d)
            }

            override fun onError(e: Throwable?) {
                resourceLiveData.value = Resource.message(R.string.save_error)
            }
        })

    }

    private fun uploadScoreToFirebase(score: ChildScore) {
        repository.saveScoreToFirebase(score).subscribe(object: CompletableObserver{
            override fun onComplete() {
                resourceLiveData.value = Resource.saved()
            }

            override fun onSubscribe(d: Disposable?) {
                compositeDisposable.add(d)
            }

            override fun onError(e: Throwable?) {
                resourceLiveData.value = Resource.message(R.string.save_error)
            }
        })
    }
}