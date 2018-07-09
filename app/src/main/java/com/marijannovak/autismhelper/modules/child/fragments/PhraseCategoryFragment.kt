package com.marijannovak.autismhelper.modules.child.fragments


import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.LinearLayout
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.InjectableFragment
import com.marijannovak.autismhelper.data.models.PhraseCategory
import com.marijannovak.autismhelper.modules.child.AACActivity
import com.marijannovak.autismhelper.modules.child.adapters.AACAdapter
import com.marijannovak.autismhelper.modules.child.adapters.PhraseCategoryAdapter
import com.marijannovak.autismhelper.modules.child.mvvm.AACViewModel
import kotlinx.android.synthetic.main.fragment_phrase_category.*

class PhraseCategoryFragment: InjectableFragment<AACViewModel>() {

    private var phraseCategoryAdapter: PhraseCategoryAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_phrase_category, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let {
            viewModel.phraseCategoryLiveData.observe(this, Observer { setUpCategories(it) })
            viewModel.loadPhraseCategories()
        }

    }

    private fun setUpCategories(categories: List<PhraseCategory>?) {
        categories?.let {
            if(phraseCategoryAdapter == null || rvPhraseCategories.adapter == null) {
                phraseCategoryAdapter = PhraseCategoryAdapter(activity!!, categories, {
                    phraseCategory, position ->
                        if(position < phraseCategoryAdapter!!.datasetCount()) {
                            (activity as AACActivity).loadFragment(PhrasePickFragment.newInstance(phraseCategory.id))
                        } else {
                            (activity as AACActivity).loadFragment(SentencePickFragment())
                        }
                }) { _, _ ->

                }
                rvPhraseCategories.adapter = phraseCategoryAdapter
                rvPhraseCategories.layoutManager = GridLayoutManager(activity, 4)
                rvPhraseCategories.itemAnimator = DefaultItemAnimator()
            }

            phraseCategoryAdapter!!.update(it)
        }
    }

}
