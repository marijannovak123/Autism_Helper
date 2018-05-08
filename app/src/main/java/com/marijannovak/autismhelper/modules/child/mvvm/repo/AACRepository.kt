package com.marijannovak.autismhelper.modules.child.mvvm.repo

import com.marijannovak.autismhelper.data.database.dao.AACDao
import com.marijannovak.autismhelper.data.models.AacPhrase
import com.marijannovak.autismhelper.utils.handleThreading
import io.reactivex.Flowable
import javax.inject.Inject

class AACRepository @Inject constructor(private val aacDao: AACDao) {

    fun getPhrases(): Flowable<List<AacPhrase>> {
        return aacDao
                .getAllPhrases()
                .handleThreading()
    }
}