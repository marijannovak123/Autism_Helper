package com.marijannovak.autismhelper.modules.child.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseAdapter
import com.marijannovak.autismhelper.common.base.BaseViewHolder
import com.marijannovak.autismhelper.data.models.AacPhrase
import kotlinx.android.synthetic.main.list_item_aac_phrase.view.*

class AACAdapter(
        aacPhrases: List<AacPhrase>,
        onItemClick: (AacPhrase, Int) -> Unit,
        onLongItemClick: (AacPhrase, Int) -> Unit
) : BaseAdapter<AACAdapter.AACViewHolder, AacPhrase>(aacPhrases.toMutableList(), onItemClick, onLongItemClick) {

    override fun createHolder(parent: ViewGroup, viewType: Int): AACViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_aac_phrase, parent, false)
        return AACViewHolder(view)
    }

    class AACViewHolder(itemView: View) : BaseViewHolder<AacPhrase>(itemView) {
        override fun bind(model: AacPhrase, position: Int, onItemClick: (AacPhrase, Int) -> Unit, onLongItemClick: (AacPhrase, Int) -> Unit) {
            with(itemView) {
                tvAacName.text = model.text

                Glide.with(context)
                        .load(model.iconPath)
                        .into(ivAacImg)

                setOnClickListener { onItemClick(model, position) }
                setOnLongClickListener {
                    onLongItemClick(model, position)
                    true
                }
            }
        }

    }
}