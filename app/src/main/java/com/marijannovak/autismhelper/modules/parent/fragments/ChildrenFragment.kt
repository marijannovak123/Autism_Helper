package com.marijannovak.autismhelper.modules.parent.fragments


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseFragment
import com.marijannovak.autismhelper.common.enums.Status
import com.marijannovak.autismhelper.data.models.Child
import com.marijannovak.autismhelper.data.models.UserChildrenJoin
import com.marijannovak.autismhelper.modules.parent.ParentActivity
import com.marijannovak.autismhelper.modules.parent.adapters.ChildrenAdapter
import com.marijannovak.autismhelper.modules.parent.mvvm.ParentViewModel
import com.marijannovak.autismhelper.utils.DialogHelper
import com.marijannovak.autismhelper.utils.Resource
import kotlinx.android.synthetic.main.fragment_children.*

class ChildrenFragment : BaseFragment() {

    private lateinit var parentViewModel: ParentViewModel
    private var adapter: ChildrenAdapter? = null
    private var userWithChildren: UserChildrenJoin? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_children, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.let {
            parentViewModel = ViewModelProviders.of(it).get(ParentViewModel::class.java)
            parentViewModel.resourceLiveData.observe(this,
                    Observer {
                        setUpChildrenRv(it)
                    })
        }

        parentViewModel.loadUserWithChildren()
    }

    private fun setUpChildrenRv(resource: Resource<List<UserChildrenJoin>>?) {
        resource?.let {
            if(it.status == Status.SUCCESS && it.data != null) {
                userWithChildren = it.data[0]
                if(adapter == null) {
                    adapter = ChildrenAdapter(emptyList(), { child -> openChildDetailsFragment(child)})
                    rvChildren.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                    rvChildren.itemAnimator = DefaultItemAnimator()
                    rvChildren.addItemDecoration(DividerItemDecoration(activity, LinearLayoutManager.VERTICAL))
                    rvChildren.adapter = adapter
                }

                val children = it.data[0].children
                adapter!!.update(children)
            }
        }
    }

    private fun openChildDetailsFragment(child: Child) {
        (activity as ParentActivity).loadFragment(ChildDetailsFragment.newInstance(child))
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_children, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when(it.itemId) {
                R.id.action_add_child -> {
                    userWithChildren?.let {
                        DialogHelper.showAddChildDialog(activity as ParentActivity, it.user.id, it.children.size, false, {
                            child, _ ->
                                parentViewModel.saveChild(child)
                        })
                    }

                }
            }
            true
        }
        return super.onOptionsItemSelected(item)
    }
}
