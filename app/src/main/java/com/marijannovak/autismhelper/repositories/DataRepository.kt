package com.marijannovak.autismhelper.repositories

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.StorageReference
import com.marijannovak.autismhelper.App
import com.marijannovak.autismhelper.config.Constants
import com.marijannovak.autismhelper.data.database.AppDatabase
import com.marijannovak.autismhelper.data.database.datasource.DataSource
import com.marijannovak.autismhelper.data.database.datasource.UserDataSource
import com.marijannovak.autismhelper.data.models.*
import com.marijannovak.autismhelper.data.network.API
import com.marijannovak.autismhelper.data.network.service.DataService
import com.marijannovak.autismhelper.data.network.service.UserService
import com.marijannovak.autismhelper.ui.fragments.SettingsFragment
import com.marijannovak.autismhelper.utils.PrefsHelper
import com.marijannovak.autismhelper.utils.logTag
import io.reactivex.Completable
import io.reactivex.Scheduler
import org.jetbrains.anko.doAsync
import java.io.File
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Marijan on 26.3.2018..
 */
class DataRepository @Inject constructor(
        private val api: API,
        private val auth: FirebaseAuth,
        private val dataService: DataService,
        private val dataSource: DataSource,
        private val storage: StorageReference,
        private val db: AppDatabase,
        private val userDataSource: UserDataSource,
        private val userService: UserService,
        private val prefsHelper: PrefsHelper,
        private val context: App,
        @Named(Constants.SCHEDULER_IO)private val ioScheduler: Scheduler,
        @Named(Constants.SCHEDULER_MAIN) private val mainScheduler: Scheduler
) {

    private lateinit var questionsWithImgs: List<Question>
    private lateinit var phrases: List<AacPhrase>
    private lateinit var onDataDownloaded: () -> Unit
    private lateinit var onError: (Throwable) -> Unit

    private var files: Array<String>

    init {
        files = context.filesDir.list()
    }

    suspend fun syncData(firstSync: Boolean) {
        val categories = dataService.getCategories()
        val questions = dataService.getQuestions()
        val phrases = dataService.getPhrases()
        val phraseCategories = dataService.getPhraseCategories()

        val questionsWithImgs = ArrayList<Question>()
        questions.forEach {
            if(it.categoryId == 2) {//todo: to constant!
                questionsWithImgs += it
            }
        }
        
        this.questionsWithImgs = questionsWithImgs
        this.phrases = phrases

        val contentWrapper = ContentWrapper(categories, questions, phrases, phraseCategories)
        dataSource.saveContent(contentWrapper, firstSync)
    }

    fun logOut(): Completable {
        auth.signOut()
        prefsHelper.setParentPassword("")
        return Completable.fromAction {
                db.userDao().deleteTable()
                db.childDao().deleteTable()
                db.childScoreDao().deleteTable()
                db.savedSentenceDao().deleteTable()
        }.subscribeOn(ioScheduler).observeOn(mainScheduler)
    }

    fun getParentPassword(): String = prefsHelper.getParentPassword()

    fun saveParentPassword(password: String): Completable {
        prefsHelper.setParentPassword(password)

        return db.userDao().getCurrentUser()
                .flatMapCompletable {
                    user ->
                        Completable.mergeArray(
                        api.updateParentPassword(user.id, ParentPasswordRequest(password)),
                        Completable.fromAction {
                            db.userDao().insert(user)
                        }
            )
        }.subscribeOn(ioScheduler).observeOn(mainScheduler)
    }

    fun downloadImages(onComplete: () -> Unit, onError: (t: Throwable) -> Unit) {
        this.onDataDownloaded = onComplete
        this.onError = onError
        downloadQuestionImage(0)
    }

    private fun downloadQuestionImage(pos: Int) {
        val question = questionsWithImgs[pos]
        files = context.filesDir.list()
        val filename = "${question.extraData}"
        val file = File(App.getAppContext().filesDir, filename)
        if (!files.contains(question.extraData)) {
            Log.e(logTag(), "Downloading ${questionsWithImgs[pos].extraData}")
            val ref = storage.child(question.extraData!!)
            ref.getFile(file)
                    .addOnSuccessListener {
                        updateQuestionImgPath(question, file.absolutePath).subscribe(
                                {
                                    if (pos == questionsWithImgs.size - 1) {
                                        downloadPhraseImage(0)
                                    } else {
                                        downloadQuestionImage(pos + 1)
                                    }
                                },
                                {
                                    onError(it)
                                    Log.e(logTag(), "FAIL ${question.extraData!!}")
                                }
                        )
                    }
                    .addOnFailureListener {
                        onError(it)
                        Log.e(logTag(), "FAIL ${question.extraData!!}")
                    }
        } else {
            updateQuestionImgPath(question, file.absolutePath).subscribe {
                if (pos == questionsWithImgs.size - 1) {
                    downloadPhraseImage(0)
                } else {
                    downloadQuestionImage(pos + 1)
                }
            }
        }

    }

    private fun downloadPhraseImage(pos: Int) {
        val phrase = phrases[pos]
        files = context.filesDir.list()
        val filename = "${phrase.iconPath}.jpg"
        val file = File(App.getAppContext().filesDir, filename)
        if (!files.contains(phrase.iconPath)) {
            Log.e(logTag(), "Downloading ${phrases[pos].iconPath}")
            val ref = storage.child(phrase.iconPath)
            ref.getFile(file)
                    .addOnSuccessListener {
                        updatePhraseImgPath(phrase, file.absolutePath).subscribe(
                                {
                                    if (pos == phrases.size - 1) {
                                        downloadProfilePic()
                                    } else {
                                        downloadPhraseImage(pos + 1)
                                    }
                                },
                                {
                                    onError(it)
                                    Log.e(logTag(), "FAIL ${phrase.iconPath}")
                                }
                        )
                    }
                    .addOnFailureListener {
                        onError(it)
                        Log.e(logTag(), "FAIL ${phrase.iconPath}")
                    }
        } else {
            updatePhraseImgPath(phrase, file.absolutePath).subscribe{
                if (pos == phrases.size - 1) {
                    downloadProfilePic()
                } else {
                    downloadPhraseImage(pos + 1)
                }
            }
        }

    }

    private fun downloadProfilePic() {
        db.userDao()
                .getCurrentUser()
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .subscribe({ user ->
                    if(user.profilePicPath.isNullOrEmpty()) {
                        onDataDownloaded()
                    } else {
                        val filename = "${user.id}${System.currentTimeMillis()}.jpg"
                        val file = File(App.getAppContext().filesDir, filename)
                        val ref = storage.child(user.profilePicPath!!)
                        ref.getFile(file)
                                .addOnSuccessListener {
                                    user.profilePicPath = file.absolutePath
                                    doAsync { db.userDao().insert(user) }
                                    onDataDownloaded()
                                }.addOnFailureListener{onDataDownloaded()}
                    }
                }, {
                    onDataDownloaded()
                })
}

    private fun updatePhraseImgPath(phrase: AacPhrase, absolutePath: String): Completable {
        phrase.iconPath = absolutePath
        return Completable.fromAction {
            db.aacDao().insert(phrase)
        }.subscribeOn(ioScheduler).observeOn(mainScheduler)
    }

    private fun updateQuestionImgPath(question: Question, path: String): Completable {
        question.imgPath = path
        return Completable.fromAction {
            db.questionDao().insert(question)
        }.subscribeOn(ioScheduler).observeOn(mainScheduler)
    }

    suspend fun syncUserData() {
        val currentUserId = userDataSource.getLoggedInUser().id
        val userData = userService.getUserData(currentUserId)
        userDataSource.updateUser(userData)

        val onlineScores = userData.childScores?.values?.toList() ?: emptyList()
        val scoresToUpload = userDataSource.getScoresToUpload(onlineScores)
        if(scoresToUpload.isNotEmpty()) {
            userService.uploadScores(currentUserId, scoresToUpload)
        }
    }

//    private fun updateUser(user: User) {
//
//        return Completable.fromAction {
//            db.userDao().update(user.username!!, user.parentPassword!!)
//            user.children?.let {
//                db.childDao().updateMultiple(it.mapToList())
//            }
//
//            user.childScores?.let {
//                db.childScoreDao().insertMultiple(it.mapToList())
//                checkIfAnyScoresShouldBeUploaded(it.mapToList())
//            }
//            prefsHelper.setParentPassword(user.parentPassword ?: "")
//        }
//    }

    fun isSoundOn(): Boolean {
        return prefsHelper.isSoundOn()
    }

    fun getTtsSpeed(): Float {
        return prefsHelper.getTtsSpeed()
    }

    fun getTtsPitch(): Float {
        return prefsHelper.getTtsPitch()
    }

    fun saveSettings(settings: SettingsFragment.Settings) {
        prefsHelper.setSoundsOn(settings.soundOn)
        prefsHelper.setTtsPitch(settings.ttsPitch)
        prefsHelper.setTtsSpeed(settings.ttsSpeed)
    }

}