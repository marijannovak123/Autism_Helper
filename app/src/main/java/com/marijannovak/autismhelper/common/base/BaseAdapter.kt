package com.marijannovak.autismhelper.common.base

import android.animation.ObjectAnimator
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator

abstract class BaseAdapter<VH : BaseViewHolder<T>, T>(
        protected var dataSet: MutableList<T>,
        protected var onItemClick: (T, Int) -> Unit,
        protected var onLongItemClick: (T, Int) -> Unit)
    : RecyclerView.Adapter<VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return createHolder(parent, viewType)
    }

    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(dataSet[position], position, onItemClick, onLongItemClick)
    }

    fun update(dataSet: List<T>) {
        this.dataSet = dataSet.toMutableList()
        this.notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        this.dataSet.removeAt(position)
        this.notifyDataSetChanged()
    }

    fun addItem(model: T) {
        this.dataSet.add(model)
        this.notifyDataSetChanged()
    }

    fun addItems(models: List<T>) {
        this.dataSet.addAll(models)
        this.notifyDataSetChanged()
    }

    protected abstract fun createHolder(parent: ViewGroup, viewType: Int): VH

    fun datasetCount() = dataSet.size

    fun getDataset() = dataSet

    protected fun rotationAnimation(view: View, from: Float, to: Float): ObjectAnimator {
        return  ObjectAnimator.ofFloat(view, "rotation", from, to)
                .apply {
                    duration = 300
                    interpolator = LinearInterpolator()
                }
    }
}