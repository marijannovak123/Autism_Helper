package com.marijannovak.autismhelper.modules.parent.mvvm

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.marijannovak.autismhelper.data.database.dao.ChildDao
import com.marijannovak.autismhelper.data.database.dao.ChildScoreDao
import com.marijannovak.autismhelper.data.models.Child
import com.marijannovak.autismhelper.data.models.ChildScore
import com.marijannovak.autismhelper.data.network.API
import com.marijannovak.autismhelper.utils.handleThreading
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject

class ParentRepository @Inject constructor(
        private val childDao: ChildDao,
        private val api: API,
        private val childScoreDao: ChildScoreDao
){
    fun saveChildLocallyAndOnline(child: Child): Completable {
        return Completable.mergeArray(
                api.addChild(child.parentId, child.id, child),
                Completable.fromAction {
                    childDao.insert(child)
                }
        ).handleThreading()
    }

    fun loadChildScoresLineData(childId: String): Flowable<LineData> {
        return childScoreDao
                .getChildScores(childId)
                .map {
                   createLineData(it)
                }
                .handleThreading()
    }

    private fun createLineData(scores: List<ChildScore>): LineData {
        var entries: List<Entry> = emptyList()
        for(i in 0 until scores.size) {
            entries += Entry(scores[i].timestamp.toFloat(), scores[i].duration/1000f)
        }
        val lineDataSet = LineDataSet(entries, "Seconds")
        return LineData(lineDataSet)
    }
}

