package com.marijannovak.autismhelper.modules.child.fragments


import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.InjectableFragment
import com.marijannovak.autismhelper.data.models.SavedSentence
import com.marijannovak.autismhelper.modules.child.AACActivity
import com.marijannovak.autismhelper.modules.child.adapters.SavedSentenceAdapter
import com.marijannovak.autismhelper.modules.child.mvvm.AACViewModel
import kotlinx.android.synthetic.main.fragment_sentence_pick.*

class SentencePickFragment : InjectableFragment<AACViewModel>() {

    private var sentenceAdapter: SavedSentenceAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sentence_pick, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let {
            viewModel.sentenceLivedata.observe(this, Observer { setUpSentences(it) })
            viewModel.loadSentences()
        }
    }

    private fun setUpSentences(sentences: List<SavedSentence>?) {
        sentences?.let {
            if(it.isEmpty()) {
                rvSentences.visibility = View.GONE
                tvNoSentences.visibility = View.VISIBLE
            } else {
                rvSentences.visibility = View.VISIBLE
                tvNoSentences.visibility = View.GONE
                if(sentenceAdapter == null || rvSentences.adapter == null) {
                    sentenceAdapter = SavedSentenceAdapter(it, {
                        sentence, _ ->
                        (activity as AACActivity).addMultipleItemsToDisplay(sentence.phrases)
                    }, { sentence, _ ->
                        viewModel.deleteSentence(sentence)
                    })
                    rvSentences.adapter = sentenceAdapter
                    rvSentences.layoutManager = LinearLayoutManager(activity)
                    rvSentences.itemAnimator = DefaultItemAnimator()
                }

                sentenceAdapter!!.update(it)
            }
        }
    }
}
