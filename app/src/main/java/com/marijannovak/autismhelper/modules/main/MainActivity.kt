package com.marijannovak.autismhelper.modules.main

import androidx.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.ViewModelActivity
import com.marijannovak.autismhelper.common.enums.Status
import com.marijannovak.autismhelper.config.Constants.Companion.EXTRA_CHILD
import com.marijannovak.autismhelper.data.models.Child
import com.marijannovak.autismhelper.modules.child.PickCategoryActivity
import com.marijannovak.autismhelper.modules.login.LoginActivity
import com.marijannovak.autismhelper.modules.main.adapters.ChildPickAdapter
import com.marijannovak.autismhelper.modules.main.mvvm.MainViewModel
import com.marijannovak.autismhelper.modules.parent.ParentActivity
import com.marijannovak.autismhelper.utils.DialogHelper
import com.marijannovak.autismhelper.utils.Resource
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : ViewModelActivity<MainViewModel, List<Child>>() {

    private var childPickAdapter: ChildPickAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    override fun onStart() {
        super.onStart()
        viewModel.getChildrenToPick()
    }

    private fun init() {
        cvParents.setOnClickListener { enterPasswordDialog() }
    }

    private fun startChildActivity(child: Child) {
        val intent = Intent(this, PickCategoryActivity::class.java)
        intent.putExtra(EXTRA_CHILD, child)
        startActivity(intent)
    }

    private fun enterPasswordDialog() {
        DialogHelper.showEnterParentPasswordDialog(this, viewModel.getParentPassword(), { password ->
            if (viewModel.getParentPassword() == "") {
                viewModel.saveParentPassword(password)
            } else {
                if (password == viewModel.getParentPassword()) {
                    startActivity(Intent(this@MainActivity, ParentActivity::class.java))
                    finish()
                } else {
                    showMessage(R.string.error_incorrect_password, null)
                    enterPasswordDialog()
                }
            }
        }, {
            startActivity(Intent(this@MainActivity, ParentActivity::class.java))
            finish()
        })
    }

    override fun subscribeToData() {
        viewModel.resourceLiveData.observe(this, Observer { resource -> handleResource(resource) })
    }

    override fun handleResource(resource: Resource<List<Child>>?) {
        resource?.let {
            showLoading(it.status, it.message)
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let {
                        if(it.isEmpty()) {
                            tvNoChildren.visibility == View.VISIBLE
                        } else {
                            setupPickChildRv(it)
                        }
                    }
                }

                Status.NEXT -> {
                    startActivity(Intent(this@MainActivity, ParentActivity::class.java))
                    finish()
                }

                Status.HOME -> {
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                    finish()
                }

                Status.MESSAGE -> {
                    showMessage(0, it.message)
                }

                else -> {
                    //NOOP
                }
            }
        }
    }

    private fun setupPickChildRv(it: List<Child>) {
        if(childPickAdapter == null || rvChildrenPick.adapter == null) {
            childPickAdapter = ChildPickAdapter(emptyList(), {
                child, _ ->
                    startChildActivity(child)
            })
            rvChildrenPick.adapter = childPickAdapter
            rvChildrenPick.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
            rvChildrenPick.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        }

        childPickAdapter!!.update(it)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_logout -> {
                DialogHelper.showPromptDialog(this, getString(R.string.really_logout), {
                    viewModel.logOut()
                })
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}
