package com.marijannovak.autismhelper.modules.child.mvvm

import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseViewModel
import com.marijannovak.autismhelper.data.models.CategoryQuestionsAnswersJoin
import com.marijannovak.autismhelper.data.models.Child
import com.marijannovak.autismhelper.utils.Resource
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import io.reactivex.subscribers.DisposableSubscriber
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import javax.inject.Inject

class ChildViewModel @Inject constructor(private val repository: ChildRepository)
    : BaseViewModel<CategoryQuestionsAnswersJoin>() {

    fun loadCategories() {
        resourceLiveData.value = Resource.loading()
        repository.loadCategories().subscribe(object: SingleObserver<List<CategoryQuestionsAnswersJoin>> {
            override fun onSuccess(categoriesQuestionsAnswers: List<CategoryQuestionsAnswersJoin>?) {
                resourceLiveData.value = Resource.success(categoriesQuestionsAnswers)
            }

            override fun onSubscribe(d: Disposable?) {
                compositeDisposable.add(d)
            }

            override fun onError(e: Throwable?) {
                resourceLiveData.value = Resource.message(R.string.data_load_error)
            }

        })

    }

}