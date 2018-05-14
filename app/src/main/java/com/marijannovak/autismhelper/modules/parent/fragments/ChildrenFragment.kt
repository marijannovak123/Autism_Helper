package com.marijannovak.autismhelper.modules.parent.fragments


import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.InjectableFragment
import com.marijannovak.autismhelper.common.enums.Status
import com.marijannovak.autismhelper.data.models.Child
import com.marijannovak.autismhelper.data.models.UserChildrenJoin
import com.marijannovak.autismhelper.modules.parent.ParentActivity
import com.marijannovak.autismhelper.modules.parent.adapters.ChildrenAdapter
import com.marijannovak.autismhelper.modules.parent.mvvm.ParentViewModel
import com.marijannovak.autismhelper.utils.DialogHelper
import com.marijannovak.autismhelper.utils.Resource
import kotlinx.android.synthetic.main.fragment_children.*

class ChildrenFragment : InjectableFragment<ParentViewModel>() {
    //todo: design, add more details
    private var adapter: ChildrenAdapter? = null
    private var children: List<Child>? = null

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
            viewModel.childrenLiveData.observe(this,
                    Observer {
                        setUpChildrenRv(it)
                    })
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.loadChildren()
    }

    private fun setUpChildrenRv(resource: Resource<List<Child>>?) {
        resource?.let {
            if (it.status == Status.SUCCESS && it.data != null) {
                this.children = it.data
                if (adapter == null || rvChildren.adapter == null) {
                    adapter = ChildrenAdapter(emptyList(), { child, _ ->
                        openChildDetailsFragment(child)
                    }, { child, _ ->
                        //Noop
                    })
                    rvChildren.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                    rvChildren.itemAnimator = DefaultItemAnimator()
                    rvChildren.adapter = adapter
                }
                adapter!!.update(it.data)
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
            when (it.itemId) {
                R.id.action_add_child -> {
                    children?.let {
                        DialogHelper.showAddChildDialog(activity as ParentActivity, it[0].parentId, it.size, false,
                                { child, _ -> viewModel.saveChild(child) },
                                { /*NOOP*/ }
                        )
                    }

                }

                else -> {}
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
