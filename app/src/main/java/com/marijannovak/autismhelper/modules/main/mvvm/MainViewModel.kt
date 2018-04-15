package com.marijannovak.autismhelper.modules.main.mvvm

import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseViewModel
import com.marijannovak.autismhelper.data.models.Category
import com.marijannovak.autismhelper.data.repo.DataRepository
import com.marijannovak.autismhelper.data.repo.IDataRepository
import com.marijannovak.autismhelper.utils.Resource
import io.reactivex.subscribers.DisposableSubscriber

/**
 * Created by Marijan on 23.3.2018..
 */
class MainViewModel(private val repository : IMainRepository,
                    dataRepository: IDataRepository)
    : BaseViewModel<Category>(dataRepository) {

    constructor() : this(MainRepository(), DataRepository())

    fun loadCategories() {
        resourceLiveData.value = Resource.loading()
        compositeDisposable.add(
                repository.loadCategories().subscribeWith(object : DisposableSubscriber<List<Category>>() {
                    override fun onComplete() {
                        resourceLiveData.value = Resource.success(null)
                    }

                    override fun onNext(categories: List<Category>?) {
                        if(categories != null) {
                            resourceLiveData.value = Resource.success(categories)
                        } else {
                            resourceLiveData.value = Resource.message(R.string.load_error)
                        }
                    }

                    override fun onError(t: Throwable?) {
                        t?.let {
                            resourceLiveData.value = Resource.message(it.message as String)
                        }
                    }
                })
        )
    }

}