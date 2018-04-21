package com.marijannovak.autismhelper.common.base

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

abstract class BaseAdapter<VH: BaseViewHolder<T>, T> (
        private var dataSet: List<T>,
        private var onItemClick: (T) -> Unit )
    : RecyclerView.Adapter<VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return createHolder(parent)
    }

    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(dataSet[position], onItemClick)
    }

    fun update(dataSet: List<T>) {
        this.dataSet = dataSet
        notifyDataSetChanged()
    }

    protected abstract fun createHolder(parent: ViewGroup): VH
}