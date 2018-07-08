package com.marijannovak.autismhelper.modules.child.fragments


import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.InjectableFragment
import com.marijannovak.autismhelper.data.models.PhraseCategory
import com.marijannovak.autismhelper.modules.child.adapters.PhraseCategoryAdapter
import com.marijannovak.autismhelper.modules.child.mvvm.AACViewModel
import kotlinx.android.synthetic.main.fragment_phrase_category.*

class PhraseCategoryFragment : InjectableFragment<AACViewModel>() {

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
                phraseCategoryAdapter = PhraseCategoryAdapter(categories, {_, _ ->}, {_, _ ->})
                rvPhraseCategories.adapter = phraseCategoryAdapter
                rvPhraseCategories.layoutManager = LinearLayoutManager(activity)
                rvPhraseCategories.addItemDecoration(DividerItemDecoration(activity, LinearLayout.HORIZONTAL))
                rvPhraseCategories.itemAnimator = DefaultItemAnimator()
            }

            phraseCategoryAdapter!!.update(it)
        }
    }

}
