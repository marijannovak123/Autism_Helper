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
        openScoresListener: (Child, Int) -> Unit,
        deleteListener: (Child, Int) -> Unit,
        private val editListener: (Child, Int) -> Unit)
    : BaseAdapter<ChildrenAdapter.ChildrenViewHolder, Child>(childrenList.toMutableList(), openScoresListener, deleteListener) {

    override fun createHolder(parent: ViewGroup, viewType: Int): ChildrenViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_child, parent, false)
        view.setOnClickListener { }
        return ChildrenViewHolder(view, dataSet.size, editListener)
    }

    inner class ChildrenViewHolder(itemView: View, itemCount: Int, editListener: (Child, Int) -> Unit)
        : BaseViewHolder<Child>(itemView) {

        private val expandStates = Array(itemCount, { false })

        override fun bind(child: Child, position: Int, onItemClick: (Child, Int) -> Unit, onLongItemClick: (Child, Int) -> Unit) {
            with(itemView) {
                tvChildName.text = child.name
                tvDateOfBirth.text = child.dateOfBirth.toDateString()

                Glide.with(context)
                        .load(if(child.gender == GENDERS[0])R.drawable.ic_male else R.drawable.ic_female)
                        .into(ivChildGender)

                val expandClickListener = {
                    if(llExpanded.visibility == View.VISIBLE) {
                        rotationAnimation(ivArrow, 180f, 0f).start()
                        llExpanded.visibility = View.GONE
                        expandStates[position] = true
                    } else {
                        rotationAnimation(ivArrow, 0f, 180f).start()
                        llExpanded.visibility = View.VISIBLE
                        expandStates[position] = false
                    }
                }

                ivArrow.setOnClickListener { expandClickListener() }
                setOnClickListener {
                    if(llExpanded.visibility == View.GONE){
                        expandClickListener()
                    }
                }
                ivScores.setOnClickListener { onItemClick(child, position) }
                ivDeleteChild.setOnClickListener { onLongItemClick(child, position) }
                ivEditChild.setOnClickListener { editListener(child, position) }
            }
        }

    }

}

