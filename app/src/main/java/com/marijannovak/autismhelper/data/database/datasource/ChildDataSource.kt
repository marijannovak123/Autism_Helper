package com.marijannovak.autismhelper.data.database.datasource

import android.graphics.Color
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import com.marijannovak.autismhelper.config.Constants
import com.marijannovak.autismhelper.data.database.dao.ChildDao
import com.marijannovak.autismhelper.data.database.dao.ChildScoreDao
import com.marijannovak.autismhelper.data.models.ChartData
import com.marijannovak.autismhelper.data.models.Child
import com.marijannovak.autismhelper.data.models.ChildScore
import com.marijannovak.autismhelper.utils.toDayMonthString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.reactive.openSubscription
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChildDataSource @Inject constructor(
        private val childDao: ChildDao,
        private val childScoreDao: ChildScoreDao
) {
    suspend fun childrenChannel(): ReceiveChannel<List<Child>> {
        return withContext(Dispatchers.IO) {
            childDao.getChildren().openSubscription()
        }
    }

    suspend fun insertScore(score: ChildScore) {
        return withContext(Dispatchers.IO) {
            async {
                childScoreDao.insert(score)
            }.await()
        }
    }

    suspend fun insertChild(child: Child) {
        return withContext(Dispatchers.IO) {
            async {
                childDao.insert(child)
            }.await()
        }
    }

    suspend fun getScoresLineDataChannel(child: Child): ReceiveChannel<ChartData> {
        return withContext(Dispatchers.IO) {
            childScoreDao.getChildScores(child.id).map { createLineData(it, child) }
                    .distinctUntilChanged().openSubscription()
        }
    }

    private fun createLineData(scores: List<ChildScore>, child: Child): ChartData {
        val sortedScores = scores.sortedBy { it.timestamp }
        var lineEntries: List<Entry> = emptyList()
        var barEntries: List<BarEntry> = emptyList()
        var dates: List<String> = emptyList()

        sortedScores.forEachIndexed { i, score ->
            lineEntries += Entry(i.toFloat(), score.duration / 1000f)
            barEntries += BarEntry(i.toFloat(), score.mistakes.toFloat())
            dates += score.timestamp.toDayMonthString()
        }

        val lineDataSet = LineDataSet(lineEntries, "Duration in seconds")
        lineDataSet.color = if(child.gender == Constants.GENDERS[0]) Color.CYAN else Color.parseColor("#FF69B4")
        lineDataSet.lineWidth = 5f
        val durationData = LineData(lineDataSet)

        val barDataSet = BarDataSet(barEntries, "Mistakes")
        barDataSet.colors = if(child.gender == Constants.GENDERS[0]) ColorTemplate.MATERIAL_COLORS.toList() else ColorTemplate.JOYFUL_COLORS.toList()
        val mistakeData = BarData(barDataSet)

        return ChartData(durationData, mistakeData, dates)
    }

    suspend fun deleteChild(child: Child) {
        return withContext(Dispatchers.IO) {
            async {
                childDao.delete(child)
            }.await()
        }
    }

    suspend fun updateChild(child: Child) {
        return withContext(Dispatchers.IO) {
            async {
                childDao.update(child)
            }.await()
        }
    }
}