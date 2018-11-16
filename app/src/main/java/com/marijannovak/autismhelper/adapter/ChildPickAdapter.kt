package com.marijannovak.autismhelper.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseAdapter
import com.marijannovak.autismhelper.common.base.BaseViewHolder
import com.marijannovak.autismhelper.config.Constants.Companion.GENDERS
import com.marijannovak.autismhelper.data.models.Child
import kotlinx.android.synthetic.main.list_item_children_pick.view.*

class ChildPickAdapter(
        children: List<Child>,
        onItemClick: (Child, Int) -> Unit
) : BaseAdapter<ChildPickAdapter.ChildPickViewHolder, Child>(
        children.toMutableList(), onItemClick, {_, _ ->}){

    override fun createHolder(parent: ViewGroup, viewType: Int): ChildPickViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_children_pick, parent, false)
        return ChildPickViewHolder(view)
    }

    inner class ChildPickViewHolder(itemView: View): BaseViewHolder<Child>(itemView) {
        override fun bind(model: Child, position: Int, onItemClick: (Child, Int) -> Unit, onLongItemClick: (Child, Int) -> Unit) {
            with(itemView) {
                tvChildName.text = model.name

                Glide.with(context)
                        .load(if(model.gender == GENDERS[0]) R.drawable.ic_boy else R.drawable.ic_girl)
                        .into(ivChildAvatar)

                setOnClickListener { onItemClick(model, position) }
            }
        }
    }
}