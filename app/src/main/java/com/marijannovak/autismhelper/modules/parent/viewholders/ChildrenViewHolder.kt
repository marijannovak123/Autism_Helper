package com.marijannovak.autismhelper.modules.parent.viewholders

import android.view.View
import com.marijannovak.autismhelper.common.base.BaseViewHolder
import com.marijannovak.autismhelper.data.models.Child
import kotlinx.android.synthetic.main.list_item_child.view.*

class ChildrenViewHolder(itemView: View)
    : BaseViewHolder<Child>(itemView) {

    override fun bind(child: Child, onItemClick: (Child) -> Unit) {
        with(itemView) {
            tvChildName.text = child.name
            tvChildGender.text = child.sex
            setOnClickListener { onItemClick(child) }
        }
    }

}