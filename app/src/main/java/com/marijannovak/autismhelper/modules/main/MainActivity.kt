package com.marijannovak.autismhelper.modules.main

import android.os.Bundle
import android.util.Log
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.ViewModelActivity
import com.marijannovak.autismhelper.database.AppDatabase
import com.marijannovak.autismhelper.models.*
import com.marijannovak.autismhelper.modules.main.mvvm.MainRepository
import com.marijannovak.autismhelper.modules.main.mvvm.MainViewModel
import io.reactivex.MaybeObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainActivity : ViewModelActivity<MainViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        testDisplay()
    }

    private fun testDisplay() {
        AppDatabase
                .getUserDao()
                .getUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : MaybeObserver<User> {
                    override fun onSuccess(t: User?) {
                        t?.let { Log.e("test", it.email!!) }
                    }

                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable?) {

                    }

                    override fun onError(e: Throwable?) {
                        e?.let { Log.e("test", e.message!!) }
                    }
                })

        AppDatabase.getAnswerDao()
                .getAnswers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { answers -> for(answer : Answer in answers) Log.e("test", answer.text) }

        AppDatabase.getCategoriesDao()
                .getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { categories -> for(category : Category in categories) Log.e("test", category.name) }

        AppDatabase.getQuestionDao()
                .getQuestions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { questions -> for(question : Question in questions) Log.e("test", question.text) }


        AppDatabase.getQuestionTypeDao()
                .getQuestionTypes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { types -> for(type : QuestionType in types) Log.e("test", type.name)
                }

    }

    override fun createViewModel(): MainViewModel {
        return MainViewModel(MainRepository())
    }

    override fun subscribeToData() {

    }
}
