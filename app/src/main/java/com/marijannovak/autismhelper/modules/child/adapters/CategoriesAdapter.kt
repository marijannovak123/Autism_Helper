package com.marijannovak.autismhelper.modules.child.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseAdapter
import com.marijannovak.autismhelper.common.base.BaseViewHolder
import com.marijannovak.autismhelper.data.models.Category
import kotlinx.android.synthetic.main.list_item_category.view.*

class CategoriesAdapter(categories: List<Category>, onItemClick: (Category, Int) -> Unit)
    : BaseAdapter<CategoriesAdapter.CategoriesViewHolder, Category>(categories.toMutableList(), onItemClick) {

    override fun createHolder(parent: ViewGroup): CategoriesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_category, parent, false)
        return CategoriesViewHolder(view)
    }

    class CategoriesViewHolder(itemView: View) : BaseViewHolder<Category>(itemView) {
        override fun bind(model: Category, position: Int, onItemClick: (Category, Int) -> Unit) {
            itemView.setOnClickListener { onItemClick(model, position) }
            itemView.tvCategoryName.text = model.name
        }

    }
}