package com.marijannovak.autismhelper.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.StorageReference
import com.marijannovak.autismhelper.App
import com.marijannovak.autismhelper.data.database.datasource.DataSource
import com.marijannovak.autismhelper.data.database.datasource.UserDataSource
import com.marijannovak.autismhelper.data.models.AacPhrase
import com.marijannovak.autismhelper.data.models.ParentPasswordRequest
import com.marijannovak.autismhelper.data.models.Question
import com.marijannovak.autismhelper.data.models.Settings
import com.marijannovak.autismhelper.data.network.service.DataService
import com.marijannovak.autismhelper.data.network.service.UserService
import com.marijannovak.autismhelper.utils.Completion
import com.marijannovak.autismhelper.utils.PrefsHelper
import org.jetbrains.anko.doAsync
import java.io.File
import javax.inject.Inject

/**
 * Created by Marijan on 26.3.2018..
 */
class DataRepository @Inject constructor(
        private val auth: FirebaseAuth,
        private val dataService: DataService,
        private val dataSource: DataSource,
        private val storage: StorageReference,
        private val userDataSource: UserDataSource,
        private val userService: UserService,
        private val prefsHelper: PrefsHelper,
        private val context: App
) {

    private var questionsWithImgs: List<Question> = ArrayList()
    private var phrases: List<AacPhrase> = ArrayList()
    private lateinit var onDataDownloaded: () -> Unit
    private lateinit var onError: (Throwable) -> Unit

    private var files: Array<String>

    init {
        files = context.filesDir.list()
    }

    suspend fun syncData(firstSync: Boolean): Completion {
        return Completion.create {
            val contentWrapper = dataService.getContent()
            with(contentWrapper) {
                val questionsWithImgs = ArrayList<Question>()
                questions.forEach {
                    if(it.categoryId == 2) {//todo: to constant!
                        questionsWithImgs += it
                    }
                }

                this@DataRepository.questionsWithImgs = questionsWithImgs
                this@DataRepository.phrases = phrases
            }
            dataSource.saveContent(contentWrapper, firstSync)
        }
    }

    suspend fun logOut(): Completion {
        return Completion.create {
            auth.signOut()
            prefsHelper.setParentPassword("")
            dataSource.clearDb()
        }
    }

    fun getParentPassword(): String = prefsHelper.getParentPassword()

    suspend fun saveParentPassword(password: String): Completion {
        return Completion.create {
            prefsHelper.setParentPassword(password)

            val dbUser = userDataSource.getCurrentUser()
            dbUser.parentPassword = password

            userService.updateParentPassword(dbUser.id, ParentPasswordRequest(password))
            userDataSource.insertUserSuspending(dbUser)
        }
    }

    fun downloadImages(onComplete: () -> Unit = {}, onError: (t: Throwable) -> Unit = {} ) {
        this.onDataDownloaded = onComplete
        this.onError = onError
        downloadQuestionImage()
    }

    private fun downloadQuestionImage(pos: Int = 0) {
        if(questionsWithImgs.isNotEmpty()) {
            val question = questionsWithImgs[pos]
            files = context.filesDir.list()
            val filename = "${question.extraData}"
            val file = File(App.getAppContext().filesDir, filename)
            if (!files.contains(question.extraData)) {
                val ref = storage.child(question.extraData!!)
                ref.getFile(file)
                        .addOnSuccessListener {
                            try {
                                updateQuestionImgPath(question, file.absolutePath)
                                if (pos == questionsWithImgs.lastIndex) {
                                    downloadPhraseImage(0)
                                } else {
                                    downloadQuestionImage(pos + 1)
                                }
                            } catch (e: Exception) {
                                onError(e)
                            }
                        }.addOnFailureListener {
                            onError(it)
                        }
            } else {
                try {
                    updateQuestionImgPath(question, file.absolutePath)
                    if (pos == questionsWithImgs.lastIndex) {
                        downloadPhraseImage(0)
                    } else {
                        downloadQuestionImage(pos + 1)
                    }
                } catch (e: Exception) {
                    onError(e)
                }

            }
        } else {
            downloadPhraseImage()
        }
    }

    private fun downloadPhraseImage(pos: Int = 0) {//TODO: THIS TO INTENT SERVICE!!
        if(phrases.isNotEmpty()) {
            val phrase = phrases[pos]
            files = context.filesDir.list()
            val filename = "${phrase.iconPath}.jpg"
            val file = File(App.getAppContext().filesDir, filename)
            if (!files.contains(phrase.iconPath)) {
                val ref = storage.child(phrase.iconPath)
                ref.getFile(file)
                        .addOnSuccessListener {
                            try {
                                updatePhraseImgPath(phrase, file.absolutePath)
                                if (pos == phrases.lastIndex) {
                                    downloadProfilePic()
                                } else {
                                    downloadPhraseImage(pos + 1)
                                }
                            } catch (e: Exception) {
                                onError(e)
                            }
                        }.addOnFailureListener {
                            onError(it)
                        }
            } else {
                try {
                    updatePhraseImgPath(phrase, file.absolutePath)
                    if (pos == phrases.lastIndex) {
                        downloadProfilePic()
                    } else {
                        downloadPhraseImage(pos + 1)
                    }
                } catch (e: Exception) {
                    onError(e)
                }
            }
        } else {
            downloadProfilePic()
        }
    }

    private fun downloadProfilePic() {
        val user = userDataSource.getCurrentUserRaw()
        try {
            if(user.profilePicPath.isNullOrEmpty()) {
                onDataDownloaded()
            } else {
                val filename = "${user.id}${System.currentTimeMillis()}.jpg"
                val file = File(App.getAppContext().filesDir, filename)
                val ref = storage.child(user.profilePicPath!!)
                ref.getFile(file)
                        .addOnSuccessListener {
                            user.profilePicPath = file.absolutePath
                            doAsync {
                                userDataSource.insertUserNonSuspending(user)
                            }
                            onDataDownloaded()
                        }.addOnFailureListener{
                            onDataDownloaded()
                        }
            }
        } catch (e: Exception) {
            onDataDownloaded()
        }
    }

    private fun updatePhraseImgPath(phrase: AacPhrase, absolutePath: String) {
        dataSource.updatePhraseImgPath(phrase, absolutePath)
    }

    private fun updateQuestionImgPath(question: Question, path: String) {
        dataSource.updateQuestionImgPath(question, path)
    }

    suspend fun syncUserData(): Completion {
        return Completion.create {
            val currentUserId = userDataSource.getLoggedInUser().id
            val userData = userService.getUserData(currentUserId)
            userData?.let { userDataSource.updateUser(it) }

            val onlineScores = userData?.childScores?.values?.toList() ?: emptyList()
            val scoresToUpload = userDataSource.getScoresToUpload(onlineScores)
            if(scoresToUpload.isNotEmpty()) {
                userService.uploadScores(currentUserId, scoresToUpload)
            }
        }
    }

    fun isSoundOn(): Boolean {
        return prefsHelper.isSoundOn()
    }

    fun getTtsSpeed(): Float {
        return prefsHelper.getTtsSpeed()
    }

    fun getTtsPitch(): Float {
        return prefsHelper.getTtsPitch()
    }

    fun saveSettings(settings: Settings) {
        prefsHelper.setSoundsOn(settings.soundOn)
        prefsHelper.setTtsPitch(settings.ttsPitch)
        prefsHelper.setTtsSpeed(settings.ttsSpeed)
    }

}