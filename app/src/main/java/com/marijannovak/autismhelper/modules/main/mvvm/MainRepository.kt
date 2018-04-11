package com.marijannovak.autismhelper.modules.main.mvvm

import com.google.firebase.auth.FirebaseAuth
import com.marijannovak.autismhelper.database.AppDatabase
import com.marijannovak.autismhelper.models.Category
import com.marijannovak.autismhelper.utils.PrefsHelper
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync

/**
 * Created by Marijan on 23.3.2018..
 */
class MainRepository : IMainRepository {

    override fun loadCategories(): Flowable<List<Category>> {
        return AppDatabase.getCategoriesDao()
                .getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun logout() {
        val authService = FirebaseAuth.getInstance()
        authService.signOut()

        doAsync {
            AppDatabase.getUserDao().deleteTable()
        }

        PrefsHelper.setLoggedIn(false)
    }
}