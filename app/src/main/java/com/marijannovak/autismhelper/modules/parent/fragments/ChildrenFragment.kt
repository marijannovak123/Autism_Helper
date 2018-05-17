package com.marijannovak.autismhelper.modules.parent.fragments


import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.InjectableFragment
import com.marijannovak.autismhelper.data.models.Child
import com.marijannovak.autismhelper.data.models.UserChildrenJoin
import com.marijannovak.autismhelper.modules.parent.ParentActivity
import com.marijannovak.autismhelper.modules.parent.adapters.ChildrenAdapter
import com.marijannovak.autismhelper.modules.parent.mvvm.ParentViewModel
import com.marijannovak.autismhelper.utils.DialogHelper
import kotlinx.android.synthetic.main.fragment_children.*

class ChildrenFragment : InjectableFragment<ParentViewModel>() {
    //todo: design, add more details
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


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let {
            viewModel.userWithChildrenLiveData.observe(this,
                    Observer {
                        userWithChildren = it
                        it?.let {
                            setUpChildrenRv(it.children)
                        }
                    })
        }

        viewModel.loadUserWithChildren()

    }

    private fun setUpChildrenRv(children: List<Child>?) {
        children?.let {
                if (adapter == null || rvChildren.adapter == null) {
                    adapter = ChildrenAdapter(emptyList(), { child, _ ->
                        openChildDetailsFragment(child)
                    }, { child, _ ->
                            DialogHelper.showPromptDialog(activity as ParentActivity, getString(R.string.delete_child), {
                                viewModel.deleteChild(child)
                            } )
                    }, {
                        child, _ ->
                            DialogHelper.showEditChildDialog(activity as ParentActivity, child, getString(R.string.update_child), {
                                viewModel.updateChild(it)
                            })
                    })
                    rvChildren.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                    rvChildren.itemAnimator = DefaultItemAnimator()
                    rvChildren.adapter = adapter
                }
                adapter!!.update(it)

            rvChildren.visibility = if(it.isEmpty()) View.GONE else View.VISIBLE
            tvNoChildren.visibility = if(it.isNotEmpty()) View.GONE else View.VISIBLE
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
                    userWithChildren?.let {
                        val childrenNo = if(it.children.isEmpty()) 0 else it.children.size
                        DialogHelper.showAddChildDialog(activity as ParentActivity, it.user.id, childrenNo, false,
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
