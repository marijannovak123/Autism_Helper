package com.marijannovak.autismhelper.common.base

import androidx.recyclerview.widget.RecyclerView
import android.view.View

abstract class BaseViewHolder<T>(itemView: View)
    : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

    abstract fun bind(model: T, position: Int, onItemClick: (T, Int) -> Unit, onLongItemClick: (T, Int) -> Unit)

}