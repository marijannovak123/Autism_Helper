package com.marijannovak.autismhelper.modules.parent.fragments


import androidx.lifecycle.Observer
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.InjectableFragment
import com.marijannovak.autismhelper.data.models.FeedItem
import com.marijannovak.autismhelper.modules.parent.adapters.RssAdapter
import com.marijannovak.autismhelper.modules.parent.mvvm.ParentViewModel
import kotlinx.android.synthetic.main.fragment_rss.*

class RssFragment : InjectableFragment<ParentViewModel>() {

    private var feedAdapter: RssAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_rss, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let {
            viewModel.feedLiveData.observe(this, Observer { feeds -> setUpFeedRv(feeds) })
        }

        viewModel.fetchFeeds()
    }

    private fun setUpFeedRv(feeds: List<FeedItem>?) {
        feeds?.let {
            if(feedAdapter == null || rvFeed.adapter == null) {
                feedAdapter = RssAdapter(emptyList(), { feed, _ -> openItemInBrowser(feed.link) })
                rvFeed.adapter = feedAdapter
                rvFeed.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
                rvFeed.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
            }

            feedAdapter!!.update(it)
        }
    }

    private fun openItemInBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

}

