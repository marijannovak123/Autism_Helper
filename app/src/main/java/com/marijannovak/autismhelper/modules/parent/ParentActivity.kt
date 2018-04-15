package com.marijannovak.autismhelper.modules.parent

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.view.MenuItem
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.ViewModelActivity
import com.marijannovak.autismhelper.modules.parent.fragments.DashboardFragment
import com.marijannovak.autismhelper.modules.parent.mvvm.ParentViewModel
import com.marijannovak.autismhelper.utils.Resource
import kotlinx.android.synthetic.main.activity_parent.*

class ParentActivity : ViewModelActivity<ParentViewModel, Any>() {

    private var currentFragment: Fragment? = DashboardFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent)

        setupDrawer()
    }

    private fun setupDrawer() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)

        loadCurrentFragmnet()

        navView.setNavigationItemSelectedListener {item -> handleNavViewClick(item)  }
    }

    private fun loadCurrentFragmnet() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.llContainer, currentFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun handleNavViewClick(item: MenuItem): Boolean {
        currentFragment = null
        when(item.itemId) {

        }

        return true
    }

    //todo: VIEWMODELPROVIDERS
    override fun createViewModel() = ViewModelProviders.of(this).get(ParentViewModel::class.java)

    override fun subscribeToData() {

    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers()
        }
    }

    override fun handleResource(resource: Resource<List<Any>>?) {

    }
}
