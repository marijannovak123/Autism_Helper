package com.marijannovak.autismhelper.modules.parent.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseAdapter
import com.marijannovak.autismhelper.common.base.BaseViewHolder
import com.marijannovak.autismhelper.data.models.Child
import kotlinx.android.synthetic.main.list_item_child.view.*

class ChildrenAdapter(
        childrenList: List<Child>,
        onItemClick: (Child, Int) -> Unit,
        onLongItemClick: (Child, Int) -> Unit)
    : BaseAdapter<ChildrenAdapter.ChildrenViewHolder, Child>(childrenList.toMutableList(), onItemClick, onLongItemClick) {

    override fun createHolder(parent: ViewGroup): ChildrenViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_child, parent, false)
        view.setOnClickListener { }
        return ChildrenViewHolder(view)
    }

    class ChildrenViewHolder(itemView: View)
        : BaseViewHolder<Child>(itemView) {

        override fun bind(child: Child, position: Int, onItemClick: (Child, Int) -> Unit, onLongItemClick: (Child, Int) -> Unit) {
            with(itemView) {
                tvChildName.text = child.name
                tvChildGender.text = child.gender
                setOnClickListener { onItemClick(child, position) }
            }
        }

    }

}

