package com.marijannovak.autismhelper.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import com.google.firebase.storage.StorageReference
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseViewModel
import com.marijannovak.autismhelper.common.enums.Status
import com.marijannovak.autismhelper.data.models.*
import com.marijannovak.autismhelper.repositories.AACRepository
import com.marijannovak.autismhelper.repositories.ParentRepository
import com.marijannovak.autismhelper.utils.onCompletion
import com.marijannovak.autismhelper.utils.onError
import com.marijannovak.autismhelper.utils.onSuccess
import kotlinx.android.synthetic.main.list_item_child.view.*
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class ParentViewModel @Inject constructor(
        private val repository: ParentRepository,
        private val aacRepository: AACRepository,
        private val storageRef: StorageReference
): BaseViewModel<UserChildrenJoin>() {

    var userName = ""
    val userNameLiveData = MutableLiveData<String>()
    val chartLiveData = MutableLiveData<ChartData>()
    val phraseLiveData = MutableLiveData<List<AacPhrase>>()
    val phraseCategoryLiveData = MutableLiveData<List<PhraseCategory>>()
    val userWithChildrenLiveData = MutableLiveData<UserChildrenJoin>()
    val userLiveData = MutableLiveData<User>()
    val feedLiveData = MutableLiveData<List<FeedItem>>()

    fun loadUsername() {
        uiScope.launch {
            repository.loadUserName()
                    .onSuccess{
                        userName = it
                        userNameLiveData.value = it
                        setSuccess()
                    }.onError {
                        setMessage(R.string.load_error)
                    }
        }
    }

    fun loadUserWithChildren() {
        setLoading()
        uiScope.launch {
            val userWithChildrenChannel = repository.loadUserWithChildren()
            for(userWithChildren in userWithChildrenChannel) {
                userWithChildrenLiveData.value = userWithChildren
                setSuccess()
            }
        }
    }

    fun saveChild(child: Child) {
        setLoading()
        uiScope.launch {
            repository.saveChildLocallyAndOnline(child)
                    .onSuccess { setState(Status.SAVED) }
                    .onError { setMessage(R.string.error_inserting) }
        }
    }

    fun loadChildScores(child: Child) {
        uiScope.launch {
            val childScoresLineDataChannel = repository.loadChildScoresLineData(child)
            for(childScores in childScoresLineDataChannel) {
                chartLiveData.postValue(childScores)
            }
        }
    }

    fun subscribeToPhrases() {
        setLoading()
        uiScope.launch {
            val phrasesChannel = repository.loadPhrases()
            for(phrases in phrasesChannel) {
                setSuccess()
                phraseLiveData.postValue(phrases)
            }
        }
    }

    fun savePhrase(phrase: AacPhrase) {
        setLoading()
        uiScope.launch {
            aacRepository.savePhrase(phrase)
                    .onCompletion { error ->
                        error?.let {
                            setMessage(R.string.error_saving_phrase)
                        } ?: setState(Status.SAVED)
                    }
        }
//        compositeDisposable.add(
//                aacRepository.savePhrase(phrase).subscribe(
//                        { resourceLiveData.value = Resource.saved() },
//                        { setMessage(R.string.error_saving_phrase) }
//                )
//        )
    }

    fun syncUserAndData() {
        setLoading()
        uiScope.launch {
            dataRepository.syncUserData()
                    .onCompletion { error ->
                        error?.let {
                            setMessage(R.string.sync_error)
                        } ?: syncData(false)
                    }
        }
    }

    private fun syncData(firstSync: Boolean) {
        uiScope.launch {
            dataRepository.syncData(firstSync)
                    .onCompletion { error ->
                        error?.let {
                            setMessage(R.string.sync_error)
                        } ?: downloadImages()
                    }
        }
    }

    private fun downloadImages() {
        dataRepository.downloadImages ({
            setMessage(R.string.data_synced)
        }, {
            setMessage(R.string.sync_error)
        })
    }

    fun deletePhrase(phrase: AacPhrase) {
        setLoading()
        uiScope.launch {
            aacRepository.deletePhrase(phrase)
                    .onCompletion { error ->
                        error?.let { setMessage(R.string.save_error) }
                    }
        }
    }

    fun updateUserData(userId: String, userUpdateRequest: UserUpdateRequest, picPath: String,  bitmap: Bitmap) {
        setLoading()
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val storageRef = storageRef.child("$userId.jpg")
        storageRef.putBytes(data)
                .addOnSuccessListener {
                    updateUserOnApiAndDb(userId, userUpdateRequest, picPath)
                }.addOnFailureListener{
                    setMessage(R.string.firebase_upload_error)
                }

    }

    private fun updateUserOnApiAndDb(userId: String, userUpdateRequest: UserUpdateRequest, profilePicPath: String) {
        uiScope.launch {
            repository.updateUser(userId, userUpdateRequest, profilePicPath)
                    .onCompletion { error ->
                        error?.let {
                            setMessage(R.string.save_error)
                        } ?: setMessage(R.string.saved)
                    }
        }
    }

    fun loadUser() {
        setLoading()
        uiScope.launch {
            repository.loadUser()
                    .onSuccess {
                        val user = it
                        user.parentPassword?.let {
                            if(it.isEmpty()) user.parentPassword = repository.getParentPassword()
                        }
                        userLiveData.value = user
                        setSuccess()
                    }.onError {
                        setMessage(R.string.fetch_error)
                    }
        }
    }

    fun deleteChild(child: Child) {
        setLoading()
        uiScope.launch {
            repository.deleteChild(child).onCompletion{ error ->
                error?.let {
                    setMessage(R.string.error)
                } ?: setMessage(R.string.child_deleted)
            }
        }
    }

    fun updateChild(child: Child) {
        setLoading()
        uiScope.launch {
            repository.updateChild(child)
                    .onCompletion { error ->
                        error?.let {
                            setMessage(R.string.error)
                        } ?: setMessage(R.string.child_updated)
                    }
        }
    }

    fun saveSettings(settings: Settings) {
        dataRepository.saveSettings(settings)
        setState(Status.SAVED)
    }

    fun fetchFeeds() {
        setLoading()
        uiScope.launch {
            repository.fetchFeeds()
                    .onSuccess {
                        feedLiveData.value = it
                        setSuccess()
                    }.onError {
                        setMessage(R.string.fetch_error)
                    }
        }
    }

    fun loadPhraseCategories() {
        uiScope.launch {
            val phraseCategoryChannel = aacRepository.loadPhraseCategories()
            for(phraseCategories in phraseCategoryChannel) {
                phraseCategoryLiveData.postValue(phraseCategories)
            }
        }
    }

}