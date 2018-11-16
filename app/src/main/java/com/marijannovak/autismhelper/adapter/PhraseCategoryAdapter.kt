package com.marijannovak.autismhelper.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseAdapter
import com.marijannovak.autismhelper.common.base.BaseViewHolder
import com.marijannovak.autismhelper.data.models.PhraseCategory
import kotlinx.android.synthetic.main.list_item_aac_phrase.view.*

class PhraseCategoryAdapter(
        private var context: Context,
        private var phraseCategories: List<PhraseCategory>,
        private var onItemClickListener: (PhraseCategory, Int) -> Unit,
        private var onLongItemClickListener: (PhraseCategory, Int) -> Unit
): BaseAdapter<PhraseCategoryAdapter.PhraseCategoryViewHolder, PhraseCategory>(
        phraseCategories.toMutableList(),
        onItemClickListener,
        onLongItemClickListener
) {
    override fun createHolder(parent: ViewGroup, viewType: Int): PhraseCategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_aac_category, parent, false)
        return PhraseCategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhraseCategoryViewHolder, position: Int) {
        if(position < phraseCategories.size) {
            holder.bind(phraseCategories[position], position, onItemClickListener, onLongItemClickListener)
        } else {
            holder.bind(PhraseCategory(position, context.getString(R.string.sentences)), position, onItemClickListener, onLongItemClickListener)
        }
    }

    override fun getItemCount() = phraseCategories.size + 1

    inner class PhraseCategoryViewHolder(itemView: View): BaseViewHolder<PhraseCategory>(itemView) {
        override fun bind(model: PhraseCategory, position: Int, onItemClick: (PhraseCategory, Int) -> Unit, onLongItemClick: (PhraseCategory, Int) -> Unit) {
            with(itemView) {
                tvAacName.text = model.name
                val imgRes = when(model.id) {
                    0 -> R.drawable.ic_self
                    1 -> R.drawable.ic_actions
                    2 -> R.drawable.ic_feelings
                    3 -> R.drawable.ic_people
                    4 -> R.drawable.ic_description
                    5 -> R.drawable.ic_things
                    else -> R.drawable.ic_phrases_other
                }

                Glide.with(context)
                        .load(imgRes)
                        .into(ivAacImg)

                setOnClickListener { onItemClickListener(model, position) }
                setOnLongClickListener { onLongItemClickListener(model, position); true}
            }
        }
    }
}