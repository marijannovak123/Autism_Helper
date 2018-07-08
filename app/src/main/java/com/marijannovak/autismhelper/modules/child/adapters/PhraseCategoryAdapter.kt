package com.marijannovak.autismhelper.modules.child.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseAdapter
import com.marijannovak.autismhelper.common.base.BaseViewHolder
import com.marijannovak.autismhelper.data.models.PhraseCategory
import kotlinx.android.synthetic.main.list_item_category.view.*

class PhraseCategoryAdapter(
        private var phraseCategories: List<PhraseCategory>,
        onItemClick: (PhraseCategory, Int) -> Unit,
        onLongItemClick: (PhraseCategory, Int) -> Unit
): BaseAdapter<PhraseCategoryAdapter.PhraseCategoryViewHolder, PhraseCategory>(
        phraseCategories.toMutableList(),
        onItemClick,
        onLongItemClick

) {
    override fun createHolder(parent: ViewGroup, viewType: Int): PhraseCategoryViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_phrase_category, parent, false)
        return PhraseCategoryViewHolder(view)
    }


    inner class PhraseCategoryViewHolder(itemView: View): BaseViewHolder<PhraseCategory>(itemView) {
        override fun bind(model: PhraseCategory, position: Int, onItemClick: (PhraseCategory, Int) -> Unit, onLongItemClick: (PhraseCategory, Int) -> Unit) {
            itemView.tvCategoryName.text = model.name
        }
    }
}