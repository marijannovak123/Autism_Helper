package com.marijannovak.autismhelper.common.base

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

abstract class BaseAdapter<VH: BaseViewHolder<T>, T> (
        private var dataSet: MutableList<T>,
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
        this.dataSet = dataSet.toMutableList()
        this.notifyDataSetChanged()
    }

    fun deleteItem(model: T) {
        this.dataSet.remove(model)
        this.notifyDataSetChanged()
    }

    fun addItem(model: T) {
        this.dataSet.add(model)
        this.notifyDataSetChanged()
    }

    protected abstract fun createHolder(parent: ViewGroup): VH
}