package com.marijannovak.autismhelper.ui.fragments

import android.app.Activity
import androidx.lifecycle.Observer
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.InjectableFragment
import com.marijannovak.autismhelper.config.Constants
import com.marijannovak.autismhelper.config.Constants.Companion.REQUEST_CODE_IMAGE_LOADED
import com.marijannovak.autismhelper.data.models.AacPhrase
import com.marijannovak.autismhelper.data.models.PhraseCategory
import com.marijannovak.autismhelper.adapter.AACAdapter
import com.marijannovak.autismhelper.ui.activities.ParentActivity
import com.marijannovak.autismhelper.viewmodels.ParentViewModel
import com.marijannovak.autismhelper.utils.DialogHelper
import com.marijannovak.autismhelper.utils.ImageHelper
import com.marijannovak.autismhelper.utils.replaceSpacesWithUnderscores
import kotlinx.android.synthetic.main.fragment_phrases.*
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.support.v4.toast
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException


class PhrasesFragment : InjectableFragment<ParentViewModel>() {

    private var phrasesAdapter: AACAdapter? = null
    private var phraseCategoryArrayAdapter: ArrayAdapter<String>? = null
    private var categories = emptyList<PhraseCategory>()
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
            val text = etPhraseText.text.toString().trim()
            val icon = ImageHelper.saveBitmap(activity, loadedBitmap, loadedBitmapName).absolutePath

            if (text.isNotEmpty() && loadedBitmap != null && loadedBitmapName.isNotEmpty() && icon.isNotEmpty()) {
                viewModel.savePhrase(AacPhrase(text.hashCode(), text.replaceSpacesWithUnderscores(), text, icon, categories[spPhraseCategories.selectedItemPosition].id))
            } else {
                toast(R.string.invalid_input)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let {
            viewModel.phraseLiveData.observe(this,
                    Observer {
                        setUpPhrasesRv(it)
                    })
            viewModel.phraseCategoryLiveData.observe(this, Observer { setUpCategorySpinner(it) })
        }
        viewModel.subscribeToPhrases()
        viewModel.loadPhraseCategories()
    }

    private fun setUpCategorySpinner(categories: List<PhraseCategory>?) {
        categories?.let {
            this.categories = it
            val categoryNames = ArrayList<String>()
            categories.forEach{
                categoryNames.add(it.name)
            }
            if(phraseCategoryArrayAdapter == null || spPhraseCategories.adapter == null) {
                phraseCategoryArrayAdapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1, categoryNames)
                spPhraseCategories.adapter = phraseCategoryArrayAdapter
            }
        }
    }

    private fun setUpPhrasesRv(phrases: List<AacPhrase>?) {
        phrases?.let {
            if (phrasesAdapter == null || rvPhrases.adapter == null) {
                phrasesAdapter = AACAdapter(emptyList(), { phrase, _ ->
                    editPhrase(phrase)
                }) { phrase, _ ->
                    DialogHelper.showPromptDialog(activity as ParentActivity, getString(R.string.delete_phrase)) {
                        viewModel.deletePhrase(phrase)
                    }
                }
                rvPhrases.adapter = phrasesAdapter
                rvPhrases.layoutManager = androidx.recyclerview.widget.GridLayoutManager(activity, 5)
                rvPhrases.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
            }

            phrasesAdapter?.update(it)
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
                    loadedBitmapName = File(ImageHelper.getImagePath(activity, it.data)).name
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
        llAddPhrase.animate().alpha(if(show) 1f else 0f).duration = 200
        rvPhrases.animate().alpha(if(show) 0f else 1f).duration = 200
        llAddPhrase.visibility = if (show) View.VISIBLE else View.GONE
        rvPhrases.visibility = if (show) View.GONE else View.VISIBLE
        this.menu!!.setGroupVisible(R.id.group_menu_phrases, !show)
        if (!show) {
            etIcon.text.clear()
            etPhraseText.text.clear()
            ivPhraseIcon.imageResource = R.drawable.img_placeholder
            loadedBitmapName = ""
            loadedBitmap = null
        }
    }

    fun isAddPhraseShown(): Boolean {
        return llAddPhrase.alpha == 1f
    }
}

