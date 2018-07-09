package com.marijannovak.autismhelper.modules.child.fragments


import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.InjectableFragment
import com.marijannovak.autismhelper.data.models.SavedSentence
import com.marijannovak.autismhelper.modules.child.mvvm.AACViewModel

class SentencePickFragment : InjectableFragment<AACViewModel>() {

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

        }
    }
}
