package com.marijannovak.autismhelper.modules.main

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.ViewModelActivity
import com.marijannovak.autismhelper.common.enums.Enums
import com.marijannovak.autismhelper.models.Category
import com.marijannovak.autismhelper.modules.login.LoginActivity
import com.marijannovak.autismhelper.modules.main.mvvm.MainRepository
import com.marijannovak.autismhelper.modules.main.mvvm.MainViewModel
import com.marijannovak.autismhelper.sync.SyncRepository
import com.marijannovak.autismhelper.utils.PromptDialog
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : ViewModelActivity<MainViewModel, Category>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PromptDialog.show(this, "Test", "yes", "no", object : () -> Unit {
            override fun invoke() {
               toast("yes")
            }
        })

        viewModel.loadCategories()
    }

    override fun createViewModel(): MainViewModel {
        return MainViewModel(MainRepository(), SyncRepository())
    }

    override fun subscribeToData() {
        viewModel.getContentLD().observe(this, Observer { categories -> setUpUi(categories!!) } )
        viewModel.getErrorLD().observe(this, Observer { throwable -> showError(throwable!!) } )
        viewModel.getStateLD().observe(this, Observer { state -> handleState(state!!) })
    }

    private fun setUpUi(categories: List<Category>) {
        for(category : Category in categories)
            Log.e("test", category.name)
    }

    override fun handleState(state: Enums.State) {
        when(state) {
            Enums.State.LOADING -> {
                pbLoading.show()
                llContent.visibility = View.GONE
            }

            Enums.State.HOME -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

            else -> {
                pbLoading.hide()
                llContent.visibility = View.VISIBLE
            }
        }
    }

    override fun showError(throwable: Throwable) {
        toast(throwable.message.toString())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            R.id.action_logout -> {
                viewModel.logout()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
