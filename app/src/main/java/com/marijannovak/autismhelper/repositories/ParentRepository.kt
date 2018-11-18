package com.marijannovak.autismhelper.repositories

import android.graphics.Color
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import com.marijannovak.autismhelper.config.Constants
import com.marijannovak.autismhelper.config.Constants.Companion.GENDERS
import com.marijannovak.autismhelper.config.Constants.Companion.RSS_URL
import com.marijannovak.autismhelper.data.database.dao.*
import com.marijannovak.autismhelper.data.database.datasource.AacDataSource
import com.marijannovak.autismhelper.data.models.*
import com.marijannovak.autismhelper.data.models.ChartData
import com.marijannovak.autismhelper.data.network.API
import com.marijannovak.autismhelper.utils.PrefsHelper
import com.marijannovak.autismhelper.utils.toDayMonthString
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.Single
import kotlinx.coroutines.channels.ReceiveChannel
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class ParentRepository @Inject constructor(
        private val childDao: ChildDao,
        private val api: API,
        private val childScoreDao: ChildScoreDao,
        private val userDao: UserDao,
        private val feedItemDao: FeedItemDao,
        private val prefsHelper: PrefsHelper,
        private val aacSource: AacDataSource,
        @Named(Constants.SCHEDULER_IO) private val ioScheduler: Scheduler,
        @Named(Constants.SCHEDULER_MAIN) private val mainScheduler: Scheduler
) {
    fun saveChildLocallyAndOnline(child: Child): Completable {
        return api.addChild(child.parentId, child.id, child)
                    .andThen{
                            childDao.insert(child)
                        }.subscribeOn(ioScheduler)
                        .observeOn(mainScheduler)
    }

    fun loadUserWithChildren(): Flowable<UserChildrenJoin> {
        return userDao.getUserWithChildren().subscribeOn(ioScheduler).observeOn(mainScheduler)
    }

    fun loadChildScoresLineData(child: Child): Flowable<ChartData> {
        return childScoreDao
                .getChildScores(child.id)
                .map {createLineData(it, child)}
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
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
        lineDataSet.color = if(child.gender == GENDERS[0]) Color.CYAN else Color.parseColor("#FF69B4")
        lineDataSet.lineWidth = 5f
        val durationData = LineData(lineDataSet)

        val barDataSet = BarDataSet(barEntries, "Mistakes")
        barDataSet.colors = if(child.gender == GENDERS[0]) ColorTemplate.MATERIAL_COLORS.toList() else ColorTemplate.JOYFUL_COLORS.toList()
        val mistakeData = BarData(barDataSet)

        return ChartData(durationData, mistakeData, dates)
    }

    suspend fun loadPhrases(): ReceiveChannel<List<AacPhrase>> {
        return aacSource.getAllPhrasesChannel()
//        return aacDao
//                .getAllPhrases()
//                .subscribeOn(ioScheduler)
//                .observeOn(mainScheduler)
    }

    fun updateUser(userId: String, userUpdateRequest: UserUpdateRequest, profilePicPath: String): Completable {
        return api
                .updateParent(userId, userUpdateRequest)
                .doOnComplete {
                    prefsHelper.setParentPassword(userUpdateRequest.parentPassword)
                    userDao.updateAll(userUpdateRequest.username, userUpdateRequest.parentPassword, profilePicPath)
                }.subscribeOn(ioScheduler).observeOn(mainScheduler)
    }

    fun loadUser(): Single<User> {
        return userDao
                .getCurrentUser()
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
    }

    fun loadUserName(): Single<String> {
        return userDao
                .getCurrentUser()
                .map { user -> user.username ?: "Parent" }
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
    }

    fun getParentPassword() : String {
        return prefsHelper.getParentPassword()
    }

    fun deleteChild(child: Child): Completable {
        return api.deleteChild(child.parentId, child.id)
                .andThen {
                    childDao.delete(child)
                }.subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
    }

    fun updateChild(child: Child): Completable {
        return api.updateChild(child.parentId, child.id, child)
                .andThen {
                    childDao.update(child)
                }.subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
    }

    fun fetchFeeds(): Single<List<FeedItem>> {
        return api.getFeed(RSS_URL)
                .onErrorResumeNext { Single.just(RSS()) }
                .doOnSuccess { feed ->
                    feed.channel.feedItems.let {
                        if(it.isNotEmpty()) {
                            feedItemDao.insertMultiple(it)
                        }
                    }
                }.flatMap {
                    feedItemDao.getItems()
                }.subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
    }
}

