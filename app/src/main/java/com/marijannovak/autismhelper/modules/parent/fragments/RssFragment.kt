package com.marijannovak.autismhelper.modules.parent.fragments


import android.arch.lifecycle.Observer
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
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
            if(feedAdapter == null) {
                feedAdapter = RssAdapter(emptyList(), { feed, _ -> openItemInBrowser(feed.url) })
                rvFeed.adapter = feedAdapter
                rvFeed.layoutManager = LinearLayoutManager(activity)
                rvFeed.itemAnimator = DefaultItemAnimator()
                rvFeed.addItemDecoration(DividerItemDecoration(activity, LinearLayoutManager.HORIZONTAL))
            }

            feedAdapter!!.update(it)
        }
    }

    private fun openItemInBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

}
