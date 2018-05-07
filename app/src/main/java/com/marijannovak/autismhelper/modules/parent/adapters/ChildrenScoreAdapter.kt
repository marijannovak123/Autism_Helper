package com.marijannovak.autismhelper.modules.parent.adapters

import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.data.models.ChildScore

class ChildrenScoreAdapter(
        private var childScores: List<ChildScore>,
        private val onItemClick: (ChildScore) -> Unit
)
    : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.list_item_question, container, false)


        container.addView(view)
        return view
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return ""
    }

    override fun getCount() = childScores.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    fun updateDataSet(childScores: List<ChildScore>) {
        this.childScores = childScores
        notifyDataSetChanged()
    }

    fun dataSetSize() = count

}