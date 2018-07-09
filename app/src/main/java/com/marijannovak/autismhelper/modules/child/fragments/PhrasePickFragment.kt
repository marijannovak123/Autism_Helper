package com.marijannovak.autismhelper.modules.child.fragments


import android.app.Activity
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.InjectableFragment
import com.marijannovak.autismhelper.config.Constants.Companion.EXTRA_PHRASE_CATEGORY
import com.marijannovak.autismhelper.data.models.AacPhrase
import com.marijannovak.autismhelper.modules.child.AACActivity
import com.marijannovak.autismhelper.modules.child.adapters.AACAdapter
import com.marijannovak.autismhelper.modules.child.mvvm.AACViewModel
import kotlinx.android.synthetic.main.fragment_phrase_pick.*

class PhrasePickFragment : InjectableFragment<AACViewModel>() {

    private var phraseCategoryId = -1
    private var adapter: AACAdapter? = null
    private lateinit var aacActivity: AACActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            phraseCategoryId = it.getInt(EXTRA_PHRASE_CATEGORY, -1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_phrase_pick, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let {
            aacActivity = it as AACActivity
            viewModel.phrasesLiveData.observe(this, Observer { setUpPhrases(it) })
            viewModel.loadPhrases(phraseCategoryId)
        }
    }

    private fun setUpPhrases(phrases: List<AacPhrase>?) {
        phrases?.let {
            if(adapter == null || rvPhrasePicker.adapter == null) {
                adapter = AACAdapter(it, {
                    phrase, _ ->
                        if(aacActivity.getDisplayedPhrasesNo() < 10) {
                            aacActivity.addItemToDisplay(phrase)
                        } else {
                            aacActivity.showMessage(R.string.max_phrases, null)
                        }
                }) { phrase, _ ->
                    aacActivity.speak(phrase.name)
                }
                rvPhrasePicker.adapter = adapter
                rvPhrasePicker.layoutManager = GridLayoutManager(activity, 5)
                rvPhrasePicker.itemAnimator = DefaultItemAnimator()
            }

            adapter!!.update(it)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(phraseCategoryId: Int) =
                PhrasePickFragment().apply {
                    arguments = Bundle().apply {
                        putInt(EXTRA_PHRASE_CATEGORY, phraseCategoryId)
                    }
                }
    }
}
