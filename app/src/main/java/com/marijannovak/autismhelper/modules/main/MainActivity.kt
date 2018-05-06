package com.marijannovak.autismhelper.modules.main

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.ViewModelActivity
import com.marijannovak.autismhelper.common.enums.Status
import com.marijannovak.autismhelper.config.Constants.Companion.EXTRA_CHILD
import com.marijannovak.autismhelper.data.models.Child
import com.marijannovak.autismhelper.modules.child.PickCategoryActivity
import com.marijannovak.autismhelper.modules.login.LoginActivity
import com.marijannovak.autismhelper.modules.main.mvvm.MainViewModel
import com.marijannovak.autismhelper.modules.parent.ParentActivity
import com.marijannovak.autismhelper.utils.DialogHelper
import com.marijannovak.autismhelper.utils.Resource
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : ViewModelActivity<MainViewModel, Child>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init() {
        btnParent.setOnClickListener { enterPasswordDialog() }
        btnChild.setOnClickListener { viewModel.getChildrenToPick() }
    }

    private fun startChildActivity(child: Child) {
        val intent = Intent(this, PickCategoryActivity::class.java)
        intent.putExtra(EXTRA_CHILD, child)
        startActivity(intent)
    }

    private fun enterPasswordDialog() {
        val listener : (String) -> Unit = { password ->
            if(viewModel.getParentPassword() == "") {
                viewModel.saveParentPassword(password)
            }
            if (password == viewModel.getParentPassword()) {
                startActivity(Intent(baseContext, ParentActivity::class.java))
            } else {
                showError(R.string.error_incorrect_password, null)
                enterPasswordDialog()
            } }

        DialogHelper.showEnterParentPasswordDialog(this, viewModel.getParentPassword(), listener)
    }

    override fun subscribeToData() {
        viewModel.resourceLiveData.observe(this, Observer { resource -> handleResource(resource) })
    }

    override fun handleResource(resource: Resource<List<Child>>?) {
        resource?.let {
            showLoading(it.status)
            when(it.status) {
                Status.SUCCESS -> {
                    pickChildDialog(it.data)
                }

                Status.HOME -> {
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                    finish()
                }

                Status.MESSAGE -> {
                    showError(0, it.message)
                }

                else -> {
                   //NOOP
                }
            }
        }
    }

    private fun pickChildDialog(children: List<Child>?) {
        children?.let {
            if(children.isNotEmpty()){
                if(children.size > 1) {
                    DialogHelper.showPickChildDialog(this, children, {child -> startChildActivity(child)} )
                } else {
                    startChildActivity(children[0])
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            R.id.action_logout -> {
                viewModel.logOut()
                true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}
