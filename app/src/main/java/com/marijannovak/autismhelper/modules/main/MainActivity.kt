package com.marijannovak.autismhelper.modules.main

import android.os.Bundle
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.ViewModelActivity
import com.marijannovak.autismhelper.database.AppDatabase
import com.marijannovak.autismhelper.models.User
import com.marijannovak.autismhelper.modules.main.mvvm.MainRepository
import com.marijannovak.autismhelper.modules.main.mvvm.MainViewModel
import io.reactivex.MaybeObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.toast

class MainActivity : ViewModelActivity<MainViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val user = AppDatabase
                .getUserDao()
                .getUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : MaybeObserver<User> {
                    override fun onSuccess(t: User?) {
                        t?.let { toast(it.email!!) }
                    }

                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable?) {
                        
                    }

                    override fun onError(e: Throwable?) {
                        e?.let { toast(e.message!!) }
                    }
                })

    }

    override fun createViewModel(): MainViewModel {
        return MainViewModel(MainRepository())
    }

    override fun subscribeToData() {

    }
}
