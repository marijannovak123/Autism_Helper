package com.marijannovak.autismhelper

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import com.google.firebase.storage.StorageReference
import com.marijannovak.autismhelper.common.base.ViewModelActivity
import com.marijannovak.autismhelper.common.fragments.LoadingDialog
import com.marijannovak.autismhelper.config.Constants
import com.marijannovak.autismhelper.data.models.AacPhrase
import com.marijannovak.autismhelper.data.models.UserChildrenJoin
import com.marijannovak.autismhelper.data.network.API
import com.marijannovak.autismhelper.modules.parent.mvvm.ParentViewModel
import com.marijannovak.autismhelper.utils.ImageHelper
import com.marijannovak.autismhelper.utils.Resource
import com.marijannovak.autismhelper.utils.replaceSpacesWithUnderscores
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_phrase_upload.*
import org.jetbrains.anko.toast
import java.io.ByteArrayOutputStream
import java.io.IOException
import javax.inject.Inject


class PhraseUploadActivity :  ViewModelActivity<ParentViewModel, UserChildrenJoin>() {

    private var loadingDialog = LoadingDialog()

    @Inject
    lateinit var api: API
    @Inject
    lateinit var storageRef: StorageReference
    private var filePath: Uri? = null
    var bitmap: Bitmap? = null
    var scaledBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phrase_upload)


        etIcon.setOnClickListener {
            val intent = Intent()
            intent.type = "image/jpg"
            intent.action = Intent.ACTION_PICK
            startActivityForResult(intent, Constants.REQUEST_CODE_IMAGE_LOADED)
        }

        btnAddPhrase.setOnClickListener {
            val name = etPhraseName.text.toString().trim()

            if (name.isNotEmpty() && scaledBitmap != null ) {
                loadingDialog.show(supportFragmentManager, "")
                uploadPhrase(AacPhrase(name.hashCode(), name.replaceSpacesWithUnderscores(), name, "${name.replaceSpacesWithUnderscores()}.jpg"))

            } else {
                toast(R.string.invalid_input)
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_CODE_IMAGE_LOADED && resultCode == Activity.RESULT_OK) {
            data?.let {
                filePath = data.data
                etIcon.setText(data.dataString)
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                    scaledBitmap = ImageHelper.scaleBitmap(bitmap)
                    ivPhraseIcon.setImageBitmap(bitmap)
                }
                catch (e: IOException)
                {
                    e.printStackTrace()
                }
            }

            }
        }


    fun uploadPhrase(phrase: AacPhrase ) {
        val baos = ByteArrayOutputStream()
        scaledBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val storageRef = storageRef.child(phrase.iconPath)

        storageRef.putBytes(data)
                .addOnSuccessListener {
                    postToApi(phrase)
                }.addOnFailureListener{
                    toast(it.message ?: "Error")
                }
    }

    fun postToApi(phrase: AacPhrase) {
        api.getPhrases()
                .map { it.size }
                .flatMapCompletable {
                    api.uploadPhrase(it, phrase)
                }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object: CompletableObserver {
                    override fun onComplete() {
                        toast("Success")
                        pbLoading?.dismiss()
                    }

                    override fun onSubscribe(d: Disposable?) {

                    }

                    override fun onError(e: Throwable?) {
                        toast(e!!.message ?: "Error")
                        pbLoading?.dismiss()

                    }

                })
    }


    override fun handleResource(resource: Resource<List<UserChildrenJoin>>?) {
    }

    override fun subscribeToData() {

    }
}
