package com.marijannovak.autismhelper.modules.parent.mvvm

import android.arch.lifecycle.MutableLiveData
import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.storage.StorageReference
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseViewModel
import com.marijannovak.autismhelper.data.models.*
import com.marijannovak.autismhelper.data.repo.DataRepository
import com.marijannovak.autismhelper.modules.child.mvvm.repo.AACRepository
import com.marijannovak.autismhelper.modules.parent.fragments.SettingsFragment
import com.marijannovak.autismhelper.utils.Resource
import com.marijannovak.autismhelper.utils.logTag
import java.io.ByteArrayOutputStream
import javax.inject.Inject
//TODO: REORGANIZE REPOSITORIES BY MODELS THEY ARE DATA SOURCE FOR
class ParentViewModel @Inject constructor(
        private val repository: ParentRepository,
        private val aacRepository: AACRepository,
        private val storageRef: StorageReference,
        private val dataRepository: DataRepository)
    : BaseViewModel<UserChildrenJoin>(dataRepository) {

    var userName = ""
    val userNameLiveData = MutableLiveData<String>()
    val chartLiveData = MutableLiveData<ParentRepository.ChartData>()
    val phraseLiveData = MutableLiveData<List<AacPhrase>>()
    val userWithChildrenLiveData = MutableLiveData<UserChildrenJoin>()
    val userLiveData = MutableLiveData<User>()
    val feedLiveData = MutableLiveData<List<FeedItem>>()

    fun loadUsername() {
        compositeDisposable.add(
                repository.loadUserName().subscribe(
                        {
                            userName = it
                            userNameLiveData.value = it
                            resourceLiveData.value = Resource.success(null)
                        },
                        {
                            Log.e(logTag(), it.message)
                            resourceLiveData.value = Resource.message(R.string.load_error, it.message ?: "")
                        }
                )
        )
    }

    fun loadUserWithChildren() {
        resourceLiveData.value = Resource.loading()
        compositeDisposable.add(
                repository.loadUserWithChildren().subscribe(
                        {
                            userWithChildrenLiveData.value = it
                            resourceLiveData.value = Resource.success(null)
                        },
                        { resourceLiveData.value = Resource.message(R.string.load_error, it.message ?: "") }
                )
        )
    }

    fun saveChild(child: Child) {
        resourceLiveData.value = Resource.loading()
        compositeDisposable.add(
                repository.saveChildLocallyAndOnline(child).subscribe(
                        { resourceLiveData.value = Resource.saved() },
                        { it -> resourceLiveData.value = Resource.message(R.string.error_inserting, it.message ?: "") }
                )
        )
    }

    fun loadChildScores(child: Child) {
        compositeDisposable.add(
                repository.loadChildScoresLineData(child).subscribe(
                        {
                            chartLiveData.value = it
                            resourceLiveData.value = Resource.success(null)
                        },
                        { resourceLiveData.value = Resource.message(R.string.load_error, it.message ?: "") }
                )
        )
    }

    fun loadPhrases() {
        resourceLiveData.value = Resource.loading()
        compositeDisposable.add(
                repository.loadPhrases().subscribe(
                        {
                            phraseLiveData.value = it
                            resourceLiveData.value = Resource.success(null)
                        },
                        { resourceLiveData.value = Resource.message(R.string.load_error, it.message ?: "") }
                )
        )
    }


    fun savePhrase(phrase: AacPhrase) {
        resourceLiveData.value = Resource.loading()
        compositeDisposable.add(
                aacRepository.savePhrase(phrase).subscribe(
                        { resourceLiveData.value = Resource.saved() },
                        { resourceLiveData.value = Resource.message(R.string.error_saving_phrase, it.message ?: "") }
                )
        )
    }

    fun syncUserAndData() {
            resourceLiveData.value = Resource.loading()
            dataRepository.syncUserData().subscribe({
               syncData(false)
            }, {
                resourceLiveData.value = Resource.message(R.string.sync_error, it.message ?: "")
            })

    }

    private fun syncData(firstSync: Boolean) {
        dataRepository.syncData(firstSync).subscribe(
                {
                    dataRepository.downloadImages ({
                        resourceLiveData.value = Resource.message(R.string.data_synced, "")
                    }, {
                        resourceLiveData.value = Resource.message(R.string.sync_error, it.message ?: "")
                    })
                },
                { resourceLiveData.value = Resource.message(R.string.sync_error, it.message ?: "") }
        )
    }

    fun deletePhrase(phrase: AacPhrase) {
        resourceLiveData.value = Resource.loading()
        aacRepository.deletePhrase(phrase).subscribe(
                { /*no need for message, the phrase is gone instantly if deleted*/ },
                { resourceLiveData.value = Resource.message(R.string.save_error, it.message ?: "") }
        )
    }

    fun updateUserData(userId: String, userUpdateRequest: UserUpdateRequest, picPath: String,  bitmap: Bitmap) {
        resourceLiveData.value = Resource.loading()
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            val storageRef = storageRef.child("$userId.jpg")
            storageRef.putBytes(data)
                    .addOnSuccessListener {
                        updateUserOnApiAndDb(userId, userUpdateRequest, picPath)
                    }.addOnFailureListener{
                        resourceLiveData.value = Resource.message(R.string.firebase_upload_error,it.message ?: "Error" )
                    }

    }

    private fun updateUserOnApiAndDb(userId: String, userUpdateRequest: UserUpdateRequest, profilePicPath: String) {
        repository.updateUser(userId, userUpdateRequest, profilePicPath).subscribe({
            resourceLiveData.value = Resource.message(R.string.saved, "")
        }, {
            resourceLiveData.value = Resource.message(R.string.save_error, it.message ?: "")
        })
    }

    fun loadUser() {
        resourceLiveData.value = Resource.loading()
        compositeDisposable.add(
                repository.loadUser().subscribe(
                        {
                            val user = it
                            user.parentPassword?.let {
                                if(it.isEmpty()) user.parentPassword = repository.getParentPassword()
                            }
                            userLiveData.value = user
                            resourceLiveData.value = Resource.success(null)
                        },
                        { resourceLiveData.value = Resource.message(R.string.fetch_error, it.message ?: "") }
                )
        )
    }

    fun deleteChild(child: Child) {
        resourceLiveData.value = Resource.loading()
        compositeDisposable.add(
                repository.deleteChild(child).subscribe({
                    resourceLiveData.value = Resource.message(R.string.child_deleted, "")
                }, {
                    resourceLiveData.value = Resource.message(R.string.error, it.message ?: "")
                })
        )
    }

    fun updateChild(child: Child) {
        resourceLiveData.value = Resource.loading()
        compositeDisposable.add(
                repository.updateChild(child).subscribe(
                        {resourceLiveData.value = Resource.message(R.string.child_updated, "")},
                        {resourceLiveData.value = Resource.message(R.string.error, it.message ?: "")}
                )
        )
    }

    fun saveSettings(settings: SettingsFragment.Settings) {
        dataRepository.saveSettings(settings)
        resourceLiveData.value = Resource.saved()
    }

    fun fetchFeeds() {
        resourceLiveData.value = Resource.loading()
        repository.fetchFeeds().subscribe({
            feedLiveData.value = it
            resourceLiveData.value = Resource.success(null)
        }, {
            resourceLiveData.value = Resource.message(R.string.fetch_error, it.message ?: "")
        })
    }

}