package com.marijannovak.autismhelper.modules.parent.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseAdapter
import com.marijannovak.autismhelper.common.base.BaseViewHolder
import com.marijannovak.autismhelper.data.models.FeedItem
import kotlinx.android.synthetic.main.list_item_feed_item.view.*

class RssAdapter(
        feeds: List<FeedItem>,
        itemClickListener: (FeedItem, Int) -> Unit
): BaseAdapter<RssAdapter.RssViewHolder, FeedItem>(feeds.toMutableList(), itemClickListener, {_,_ -> }) {

    override fun createHolder(parent: ViewGroup, viewType: Int): RssViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_feed_item, parent, false)
        return RssViewHolder(view)
    }

    inner class RssViewHolder(itemView: View): BaseViewHolder<FeedItem>(itemView) {

        private val expandStates = Array(itemCount, { false })

        override fun bind(model: FeedItem, position: Int, onItemClick: (FeedItem, Int) -> Unit, onLongItemClick: (FeedItem, Int) -> Unit) {
            with(itemView) {
                Glide.with(context)
                        .load(model.image)
                        .into(ivFeedImage)

                tvDatePublished.text = model.datePublished
                tvFeedSummary.text = model.summary
                tvTitle.text = model.title

                val expandClickListener = {
                    if(rlExpanded.visibility == View.VISIBLE) {
                        rotationAnimation(ivArrow, 180f, 0f).start()
                        rlExpanded.visibility = View.GONE
                        expandStates[position] = true
                    } else {
                        rotationAnimation(ivArrow, 0f, 180f).start()
                        rlExpanded.visibility = View.VISIBLE
                        expandStates[position] = false
                    }
                }

                ivArrow.setOnClickListener { expandClickListener() }
                setOnClickListener {
                    if(rlExpanded.visibility == View.GONE){
                        expandClickListener()
                    }
                }
                ivLink.setOnClickListener { onItemClick(model, position) }
            }
        }
    }
}