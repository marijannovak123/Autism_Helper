package com.marijannovak.autismhelper.modules.parent.fragments


import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseFragment
import com.marijannovak.autismhelper.data.models.Child
import com.marijannovak.autismhelper.data.models.UserChildrenJoin
import com.marijannovak.autismhelper.modules.parent.ParentActivity
import com.marijannovak.autismhelper.modules.parent.adapters.ChildrenAdapter
import kotlinx.android.synthetic.main.fragment_children.*

class ChildrenFragment : BaseFragment() {

    var user: UserChildrenJoin? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        arguments?.let {
            user =  it.getSerializable(KEY_USER) as UserChildrenJoin
        }

        return inflater.inflate(R.layout.fragment_children, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpChildrenRv()
    }

    private fun setUpChildrenRv() {
        user?.let {
            val adapter = ChildrenAdapter(it.children, { child -> openChildDetailsFragment(child)})
            rvChildren.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            rvChildren.itemAnimator = DefaultItemAnimator()
            rvChildren.addItemDecoration(DividerItemDecoration(activity, LinearLayoutManager.VERTICAL))
            rvChildren.adapter = adapter
        }
    }

    private fun openChildDetailsFragment(child: Child) {
        val activity = activity as ParentActivity
        activity.loadFragment(ChildDetailsFragment.newInstance(child))
    }

    companion object {
        private const val KEY_USER = "user"

        @JvmStatic
        fun newInstance(user: UserChildrenJoin): ChildrenFragment {
            return ChildrenFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(KEY_USER, user)
                }
            }
        }
    }

}
