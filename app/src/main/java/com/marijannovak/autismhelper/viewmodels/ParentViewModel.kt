package com.marijannovak.autismhelper.viewmodels

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.storage.StorageReference
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseViewModel
import com.marijannovak.autismhelper.common.enums.Status
import com.marijannovak.autismhelper.data.models.*
import com.marijannovak.autismhelper.repositories.AACRepository
import com.marijannovak.autismhelper.repositories.ParentRepository
import com.marijannovak.autismhelper.utils.Resource
import com.marijannovak.autismhelper.utils.logTag
import com.marijannovak.autismhelper.utils.onCompletion
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
        compositeDisposable.add(
                repository.loadUserName().subscribe(
                        {
                            userName = it
                            userNameLiveData.value = it
                            setSuccess()
                        },
                        {
                            setMessage(R.string.load_error)
                        }
                )
        )
    }

    fun loadUserWithChildren() {
        setLoading()
        compositeDisposable.add(
                repository.loadUserWithChildren().subscribe(
                        {
                            userWithChildrenLiveData.value = it
                            setSuccess()
                        },
                        { setMessage(R.string.load_error) }
                )
        )
    }

    fun saveChild(child: Child) {
        setLoading()
        compositeDisposable.add(
                repository.saveChildLocallyAndOnline(child).subscribe(
                        { setState(Status.SAVED) },
                        { setMessage(R.string.error_inserting) }
                )
        )
    }

    fun loadChildScores(child: Child) {
        compositeDisposable.add(
                repository.loadChildScoresLineData(child).subscribe(
                        {
                            chartLiveData.value = it
                            setSuccess()
                        },
                        { setMessage(R.string.load_error) }
                )
        )
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
        repository.updateUser(userId, userUpdateRequest, profilePicPath).subscribe({
            setMessage(R.string.saved)
        }, {
            setMessage(R.string.save_error)
        })
    }

    fun loadUser() {
        setLoading()
        compositeDisposable.add(
                repository.loadUser().subscribe(
                        {
                            val user = it
                            user.parentPassword?.let {
                                if(it.isEmpty()) user.parentPassword = repository.getParentPassword()
                            }
                            userLiveData.value = user
                            setSuccess()
                        },
                        { setMessage(R.string.fetch_error) }
                )
        )
    }

    fun deleteChild(child: Child) {
        setLoading()
        compositeDisposable.add(
                repository.deleteChild(child).subscribe({
                    setMessage(R.string.child_deleted)
                }, {
                    setMessage(R.string.error)
                })
        )
    }

    fun updateChild(child: Child) {
        setLoading()
        compositeDisposable.add(
                repository.updateChild(child).subscribe(
                        {setMessage(R.string.child_updated)},
                        {setMessage(R.string.error)}
                )
        )
    }

    fun saveSettings(settings: Settings) {
        dataRepository.saveSettings(settings)
        setState(Status.SAVED)
    }

    fun fetchFeeds() {
        setLoading()
        repository.fetchFeeds().subscribe({
            feedLiveData.value = it
            setSuccess()
        }, {
            setMessage(R.string.fetch_error)
        })
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