package com.marijannovak.autismhelper.modules.parent

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.view.MenuItem
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseFragment
import com.marijannovak.autismhelper.common.base.ViewModelActivity
import com.marijannovak.autismhelper.common.enums.Status
import com.marijannovak.autismhelper.modules.login.LoginActivity
import com.marijannovak.autismhelper.modules.main.MainActivity
import com.marijannovak.autismhelper.modules.parent.fragments.DashboardFragment
import com.marijannovak.autismhelper.modules.parent.fragments.ProfileFragment
import com.marijannovak.autismhelper.modules.parent.fragments.SettingsFragment
import com.marijannovak.autismhelper.modules.parent.mvvm.ParentViewModel
import com.marijannovak.autismhelper.utils.Resource
import kotlinx.android.synthetic.main.activity_parent.*

class ParentActivity : ViewModelActivity<ParentViewModel, Any>() {

    private var currentFragment: BaseFragment? = DashboardFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent)

        setupDrawer()
    }

    private fun setupDrawer() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)

        loadCurrentFragment()

        navView.setNavigationItemSelectedListener {item -> handleNavViewClick(item)  }
    }

    private fun loadCurrentFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.llContainer, currentFragment)
        transaction.addToBackStack(null)
        transaction.commit()

        closeDrawer()
    }

    private fun handleNavViewClick(item: MenuItem): Boolean {
        currentFragment = null

        when(item.itemId) {
            R.id.dashboard -> {
                currentFragment = DashboardFragment()
                loadCurrentFragment()
            }

            R.id.profile -> {
                currentFragment = ProfileFragment()
                loadCurrentFragment()
            }

            R.id.settings -> {
                currentFragment = SettingsFragment()
                loadCurrentFragment()
            }

            R.id.logout -> {
                viewModel.logOut()
            }

            R.id.exit -> {
                startActivity(Intent(this@ParentActivity, MainActivity::class.java))
                finish()
            }
        }

        return true
    }

    override fun subscribeToData() {
        viewModel.resourceLiveData.observe(this, Observer { resource -> handleResource(resource) })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when(item.itemId) {
                android.R.id.home -> {
                    drawerLayout.openDrawer(GravityCompat.START)
                }
            }
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        closeDrawer()
    }

    private fun closeDrawer() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers()
        }
    }

    override fun handleResource(resource: Resource<List<Any>>?) {
        resource?.let {
            when(it.status) {
                Status.HOME -> {
                    startActivity(Intent(this@ParentActivity, LoginActivity::class.java))
                    finish()
                }
                else -> {
                    showLoading(false)
                }
            }
        }
    }
}
