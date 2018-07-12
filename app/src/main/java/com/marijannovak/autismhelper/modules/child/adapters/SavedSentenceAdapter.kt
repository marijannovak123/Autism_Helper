package com.marijannovak.autismhelper.modules.child.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseAdapter
import com.marijannovak.autismhelper.common.base.BaseViewHolder
import com.marijannovak.autismhelper.data.models.SavedSentence
import kotlinx.android.synthetic.main.list_item_saved_sentence.view.*

class SavedSentenceAdapter(
        sentences: List<SavedSentence>,
        onClick: (SavedSentence, Int) -> Unit,
        onLongClick: (SavedSentence, Int) -> Unit
): BaseAdapter<SavedSentenceAdapter.SavedSentenceViewHolder, SavedSentence>(sentences.toMutableList(), onClick, onLongClick) {

    override fun createHolder(parent: ViewGroup, viewType: Int): SavedSentenceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_saved_sentence, parent, false)
        return SavedSentenceViewHolder(view)
    }

    inner class SavedSentenceViewHolder(itemView: View): BaseViewHolder<SavedSentence>(itemView) {
        override fun bind(model: SavedSentence, position: Int, onItemClick: (SavedSentence, Int) -> Unit, onLongItemClick: (SavedSentence, Int) -> Unit) {
            with(itemView) {
                val stringBuilder = StringBuilder()
                model.phrases.forEach {
                    stringBuilder.append(it.text).append(" ")
                }

                tvSentence.text = stringBuilder.toString()

                setOnClickListener { onItemClick(model, position) }
                setOnLongClickListener { onLongItemClick(model, position); true }
            }
        }
    }
}