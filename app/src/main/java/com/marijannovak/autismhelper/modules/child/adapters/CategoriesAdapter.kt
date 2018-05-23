package com.marijannovak.autismhelper.modules.child.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseAdapter
import com.marijannovak.autismhelper.common.base.BaseViewHolder
import com.marijannovak.autismhelper.data.models.Category
import kotlinx.android.synthetic.main.list_item_category.view.*

class CategoriesAdapter(
        categories: List<Category>,
        onItemClick: (Category, Int) -> Unit,
        onLongItemClick: (Category, Int) -> Unit)
    : BaseAdapter<BaseViewHolder<Category>, Category>(categories.toMutableList(), onItemClick, onLongItemClick) {

    private val normal = 112
    private val footer = 113

    override fun createHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Category> {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_category, parent, false)
        return if(viewType == normal) CategoriesViewHolder(view) else CategoriesHeaderViewHolder(view)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<Category>, position: Int) {
        if(holder is CategoriesViewHolder) {
            holder.bind(dataSet[position], position, onItemClick, onLongItemClick)
        } else {
            holder.bind(Category(), position, onItemClick, onLongItemClick)
        }
    }

    override fun getItemCount() = dataSet.size + 1

    inner class CategoriesViewHolder(itemView: View) : BaseViewHolder<Category>(itemView) {
        override fun bind(model: Category, position: Int, onItemClick: (Category, Int) -> Unit, onLongItemClick: (Category, Int) -> Unit) {
            with(itemView) {
                setOnClickListener { onItemClick(model, position) }
                tvCategoryName.text = model.name

                val drawableId = when(position) {
                    0 -> R.drawable.ic_math
                    1 -> R.drawable.ic_colors
                    2 -> R.drawable.ic_emotions
                    else -> R.drawable.ic_launcher_foreground
                }

                Glide.with(context)
                        .load(drawableId)
                        .into(ivCategoryImage)
            }
        }
    }
//AAC
    inner class CategoriesHeaderViewHolder(itemView: View): BaseViewHolder<Category>(itemView) {
        override fun bind(model: Category, position: Int, onItemClick: (Category, Int) -> Unit, onLongItemClick: (Category, Int) -> Unit) {
            with(itemView) {
                setOnClickListener { onItemClick(Category(0, "AAC", emptyList()), -1) }
                tvCategoryName.text = context.getString(R.string.aac)

                Glide.with(context)
                        .load(R.drawable.ic_conversation)
                        .into(ivCategoryImage)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(position < dataSet.size) normal else footer
    }
}