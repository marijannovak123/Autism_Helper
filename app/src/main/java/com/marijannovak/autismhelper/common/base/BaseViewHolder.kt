package com.marijannovak.autismhelper.common.base

import android.support.v7.widget.RecyclerView
import android.view.View

abstract class BaseViewHolder<T>(itemView: View)
    : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(model: T, position: Int, onItemClick: (T, Int) -> Unit, onLongItemClick: (T, Int) -> Unit)

}