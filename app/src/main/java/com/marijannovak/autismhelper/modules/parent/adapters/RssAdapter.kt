package com.marijannovak.autismhelper.modules.parent.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseAdapter
import com.marijannovak.autismhelper.common.base.BaseViewHolder
import com.marijannovak.autismhelper.data.models.FeedItem
import com.marijannovak.autismhelper.utils.logTag
import kotlinx.android.synthetic.main.list_item_feed_item.view.*
import java.text.SimpleDateFormat
import java.util.*

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

                val imgUrl = model.encoded.substring(model.encoded.indexOf("https"), model.encoded.indexOf("' alt="))

                Glide.with(context)
                        .load(imgUrl)
                        .into(ivFeedImage)

                val date = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH).parse(model.pubDate)
                val formattedDate = SimpleDateFormat("EEE, dd MMM yyyy HH:mm").format(date)

                Log.e(logTag(), imgUrl)

                tvDatePublished.text = formattedDate
                tvFeedSummary.text = model.description
                tvTitle.text = model.title

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
                tvTitle.setOnClickListener { onItemClick(model, position) }
            }
        }
    }
}