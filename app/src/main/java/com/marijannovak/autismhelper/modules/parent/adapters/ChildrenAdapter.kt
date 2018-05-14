package com.marijannovak.autismhelper.modules.parent.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseAdapter
import com.marijannovak.autismhelper.common.base.BaseViewHolder
import com.marijannovak.autismhelper.config.Constants.Companion.GENDERS
import com.marijannovak.autismhelper.data.models.Child
import com.marijannovak.autismhelper.utils.toDateString
import kotlinx.android.synthetic.main.list_item_child.view.*

class ChildrenAdapter(
        childrenList: List<Child>,
        onItemClick: (Child, Int) -> Unit,
        onLongItemClick: (Child, Int) -> Unit)
    : BaseAdapter<ChildrenAdapter.ChildrenViewHolder, Child>(childrenList.toMutableList(), onItemClick, onLongItemClick) {

    override fun createHolder(parent: ViewGroup, viewType: Int): ChildrenViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_child, parent, false)
        view.setOnClickListener { }
        return ChildrenViewHolder(view)
    }

    class ChildrenViewHolder(itemView: View)
        : BaseViewHolder<Child>(itemView) {

        override fun bind(child: Child, position: Int, onItemClick: (Child, Int) -> Unit, onLongItemClick: (Child, Int) -> Unit) {
            with(itemView) {
                tvChildName.text = child.name
                tvDateOfBirth.text = child.dateOfBirth.toDateString()

                Glide.with(context)
                        .load(if(child.gender == GENDERS[0])R.drawable.ic_male else R.drawable.ic_female)
                        .into(ivChildGender)

                setOnClickListener { onItemClick(child, position) }
            }
        }

    }

}

