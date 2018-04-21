package com.marijannovak.autismhelper.modules.parent.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseAdapter
import com.marijannovak.autismhelper.data.models.Child
import com.marijannovak.autismhelper.modules.parent.viewholders.ChildrenViewHolder

class ChildrenAdapter(childrenList: List<Child>, onItemClick: (Child) -> Unit)
        : BaseAdapter<ChildrenViewHolder, Child>(childrenList, onItemClick) {

    override fun createHolder(parent: ViewGroup): ChildrenViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_child, null)
        view.setOnClickListener {  }
        return ChildrenViewHolder(view)
    }

}

