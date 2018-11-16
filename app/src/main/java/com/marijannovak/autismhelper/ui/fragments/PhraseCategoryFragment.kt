package com.marijannovak.autismhelper.ui.fragments


import androidx.lifecycle.Observer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.InjectableFragment
import com.marijannovak.autismhelper.data.models.PhraseCategory
import com.marijannovak.autismhelper.ui.activities.AACActivity
import com.marijannovak.autismhelper.adapter.PhraseCategoryAdapter
import com.marijannovak.autismhelper.viewmodels.AACViewModel
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
                phraseCategoryAdapter = PhraseCategoryAdapter(activity!!, categories, { phraseCategory, position ->
                    if (position < phraseCategoryAdapter!!.datasetCount()) {
                        (activity as AACActivity).loadFragment(PhrasePickFragment.newInstance(phraseCategory.id))
                    } else {
                        (activity as AACActivity).loadFragment(SentencePickFragment())
                    }
                }) { _, _ ->

                }
                rvPhraseCategories.adapter = phraseCategoryAdapter
                rvPhraseCategories.layoutManager = androidx.recyclerview.widget.GridLayoutManager(activity, 4)
                rvPhraseCategories.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
            }

            phraseCategoryAdapter!!.update(it)
        }
    }

}
