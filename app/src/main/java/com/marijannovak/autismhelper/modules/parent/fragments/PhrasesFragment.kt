package com.marijannovak.autismhelper.modules.parent.fragments

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.view.*
import com.bumptech.glide.Glide
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.InjectableFragment
import com.marijannovak.autismhelper.common.enums.Status
import com.marijannovak.autismhelper.config.Constants
import com.marijannovak.autismhelper.config.Constants.Companion.REQUEST_CODE_IMAGE_LOADED
import com.marijannovak.autismhelper.data.models.AacPhrase
import com.marijannovak.autismhelper.modules.child.adapters.AACAdapter
import com.marijannovak.autismhelper.modules.parent.ParentActivity
import com.marijannovak.autismhelper.modules.parent.mvvm.ParentViewModel
import com.marijannovak.autismhelper.utils.DialogHelper
import com.marijannovak.autismhelper.utils.Resource
import kotlinx.android.synthetic.main.fragment_phrases.*
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.support.v4.toast
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class PhrasesFragment : InjectableFragment<ParentViewModel>() {

    //@Inject
    //lateinit var viewModelFactory: ViewModelProvider.Factory

    //private lateinit var parentViewModel: ParentViewModel
    private var phrasesAdapter: AACAdapter? = null
    private var loadedBitmap: Bitmap? = null
    private var loadedBitmapName: String = ""

    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_phrases, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etIcon.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_PICK
            startActivityForResult(intent, REQUEST_CODE_IMAGE_LOADED)
        }

        btnAddPhrase.setOnClickListener {
            val name = etPhraseName.text.toString().trim()
            val icon = saveBitmap(loadedBitmapName).absolutePath

            if (name.isNotEmpty() && loadedBitmap != null && loadedBitmapName.isNotEmpty() && icon.isNotEmpty()) {
                viewModel.savePhrase(AacPhrase(name.hashCode(), name, icon))
            } else {
                toast(R.string.invalid_input)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let {
            // parentViewModel = ViewModelProviders.of(it, viewModelFactory).get(ParentViewModel::class.java)
            viewModel.phraseLiveData.observe(this,
                    Observer {
                        handleResource(it)
                    })
        }
        viewModel.loadPhrases()

    }

    private fun handleResource(phrases: Resource<List<AacPhrase>>?) {
        phrases?.let {
            (activity as ParentActivity).showLoading(it.status)
            when (it.status) {
                Status.SUCCESS -> {
                    setUpPhrasesRv(it.data)
                }

                Status.SAVED -> {
                    showAddPhrase(false)
                    toast("${getString(R.string.saved)} ${it.data!![0].name}")
                }

                else -> {

                }
            }
        }
    }

    private fun setUpPhrasesRv(phrases: List<AacPhrase>?) {
        phrases?.let {
            if (phrasesAdapter == null || rvPhrases.adapter == null) {
                phrasesAdapter = AACAdapter(emptyList(), { phrase, _ ->
                    editPhrase(phrase)
                }, { phrase, _ ->
                    DialogHelper.showPromptDialog(activity as ParentActivity, getString(R.string.delete_phrase), {
                        viewModel.deletePhrase(phrase)
                    })
                })
                rvPhrases.adapter = phrasesAdapter
                rvPhrases.layoutManager = GridLayoutManager(activity, 4)
                rvPhrases.itemAnimator = DefaultItemAnimator()
            }

            phrasesAdapter!!.update(phrases)
        }
    }

    private fun editPhrase(phrase: AacPhrase) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_CODE_IMAGE_LOADED && resultCode == Activity.RESULT_OK) {
            data?.let {
                try {
                    loadedBitmap?.recycle()

                    val stream = context!!.contentResolver.openInputStream(it.data)
                    loadedBitmapName = File(getImagePath(it.data)).name
                    loadedBitmap = BitmapFactory.decodeStream(stream)
                    stream.close()

                    Glide.with(activity!!)
                            .load(loadedBitmap)
                            .into(ivPhraseIcon)

                    etIcon.setText(loadedBitmapName)

                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        this.menu = menu
        inflater?.inflate(R.menu.menu_phrases, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when (it.itemId) {
                R.id.action_add_phrase -> {
                    showAddPhrase(true)
                }

                else -> {
                }
            }

        }
        return super.onOptionsItemSelected(item)
    }

    fun showAddPhrase(show: Boolean) {
        llAddPhrase.visibility = if (show) View.VISIBLE else View.GONE
        rvPhrases.visibility = if (show) View.GONE else View.VISIBLE
        this.menu!!.setGroupVisible(R.id.group_menu_phrases, !show)
        if (!show) {
            etIcon.text.clear()
            etPhraseName.text.clear()
            ivPhraseIcon.imageResource = 0
            loadedBitmapName = ""
            loadedBitmap = null
        }
    }

    private fun getImagePath(uri: Uri): String {
        val cursor = activity?.contentResolver?.query(uri, null, null, null, null);
        cursor?.let {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            val result = cursor.getString(idx)
            cursor.close()
            return result
        }
        return uri.path
    }

    private fun saveBitmap(filename: String): File {
        val file = File(activity!!.filesDir, filename)
        if (!file.exists() && loadedBitmap != null) {
            try {
                val scaledBitmap = scaleBitmap()
                val outStream = FileOutputStream(file)
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
                outStream.flush()
                outStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return file
    }

    private fun scaleBitmap(): Bitmap {
        var bitmap = loadedBitmap
        var width = bitmap!!.width
        var height = bitmap.height
        val maxSize = 96
        when {
            width > height -> {
                // landscape
                val ratio = width.toFloat() / maxSize
                width = maxSize
                height = (height / ratio).toInt()
            }
            height > width -> {
                // portrait
                val ratio = height.toFloat() / maxSize
                height = maxSize
                width = (width / ratio).toInt()
            }
            else -> {
                // square
                height = maxSize
                width = maxSize
            }
        }

        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true)
        return bitmap
    }

    fun isAddPhraseShown(): Boolean {
        return llAddPhrase.visibility == View.VISIBLE
    }
}

