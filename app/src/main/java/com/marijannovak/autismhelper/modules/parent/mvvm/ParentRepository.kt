package com.marijannovak.autismhelper.modules.parent.mvvm

import android.graphics.Color
import com.github.mikephil.charting.data.*
import com.marijannovak.autismhelper.data.database.dao.ChildDao
import com.marijannovak.autismhelper.data.database.dao.ChildScoreDao
import com.marijannovak.autismhelper.data.models.Child
import com.marijannovak.autismhelper.data.models.ChildScore
import com.marijannovak.autismhelper.data.network.API
import com.marijannovak.autismhelper.utils.handleThreading
import com.marijannovak.autismhelper.utils.toDateString
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

    fun loadChildScoresLineData(childId: String): Flowable<ChartData> {
        return childScoreDao
                .getChildScores(childId)
                .map {
                   createLineData(it)
                }
                .handleThreading()
    }

    private fun createLineData(scores: List<ChildScore>): ChartData {
        var lineEntries: List<Entry> = emptyList()
        var barEntries: List<BarEntry> = emptyList()
        var dates: List<String> = emptyList()
        for(i in 0 until scores.size) {
            lineEntries += Entry(i.toFloat(), scores[i].duration/1000f)
            barEntries += BarEntry(i.toFloat() , scores[i].mistakes.toFloat())
            dates += scores[i].timestamp.toDateString()
        }
        val lineDataSet = LineDataSet(lineEntries, "Duration in seconds")
        lineDataSet.color = Color.GREEN
        val lineData = LineData(lineDataSet)
        val barDataSet = BarDataSet(barEntries, "Mistakes")
        barDataSet.color = Color.RED
        val barData = BarData(barDataSet)
        return ChartData(lineData, barData, dates)
    }

    data class ChartData(var lineData: LineData, var barData: BarData, var dates: List<String>)
}

