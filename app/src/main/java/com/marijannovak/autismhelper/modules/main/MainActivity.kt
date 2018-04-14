package com.marijannovak.autismhelper.modules.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.ViewModelActivity
import com.marijannovak.autismhelper.common.enums.Enums.State
import com.marijannovak.autismhelper.data.repo.DataRepository
import com.marijannovak.autismhelper.modules.child.ChildActivity
import com.marijannovak.autismhelper.modules.login.LoginActivity
import com.marijannovak.autismhelper.modules.main.mvvm.MainRepository
import com.marijannovak.autismhelper.modules.main.mvvm.MainViewModel
import com.marijannovak.autismhelper.modules.parent.ParentActivity
import com.marijannovak.autismhelper.utils.DialogHelper
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.design.snackbar

class MainActivity : ViewModelActivity<MainViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init() {
        btnParent.setOnClickListener { enterPasswordDialog() }
        btnChild.setOnClickListener { startChildActivity() }
    }

    private fun startChildActivity() {
        val intent = Intent(this, ChildActivity::class.java)
        startActivity(intent)
    }

    private fun enterPasswordDialog() {
        DialogHelper.showEnterParentPasswordDialog(this, viewModel.getParentPassword(), object : (String) -> Unit {
            override fun invoke(password: String) {
                if (viewModel.getParentPassword() == "") {
                    viewModel.saveParentPassword(password)
                }

                if (password == viewModel.getParentPassword()) {
                    startActivity(Intent(baseContext, ParentActivity::class.java))
                } else {
                    showError(Throwable(getString(R.string.error_incorrect_password)))
                    enterPasswordDialog()
                }
            }
        })
    }

    override fun createViewModel() = ViewModelProviders.of(this).get(MainViewModel::class.java)

    override fun subscribeToData() {
        viewModel.getStateLD().observe(this, Observer { state -> handleState(state!!) })
    }

    override fun handleState(state: State) {
        when(state) {
            State.LOADING -> {
                showLoading(true)
            }

            State.HOME -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

            else -> {
                showLoading(false)
            }
        }
    }

    override fun showError(throwable: Throwable) {
        snackbar(llContent, throwable.message.toString())
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

            else -> super.onOptionsItemSelected(item)
        }
    }
}
