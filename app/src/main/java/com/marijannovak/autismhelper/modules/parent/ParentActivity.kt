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
import com.marijannovak.autismhelper.data.models.UserChildrenJoin
import com.marijannovak.autismhelper.modules.login.LoginActivity
import com.marijannovak.autismhelper.modules.main.MainActivity
import com.marijannovak.autismhelper.modules.parent.fragments.*
import com.marijannovak.autismhelper.modules.parent.mvvm.ParentViewModel
import com.marijannovak.autismhelper.utils.Resource
import com.marijannovak.autismhelper.utils.logTag
import kotlinx.android.synthetic.main.activity_parent.*

class ParentActivity : ViewModelActivity<ParentViewModel, UserChildrenJoin>() {

    private var user: UserChildrenJoin? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent)

        viewModel.loadUser()
        setupDrawer()
    }

    private fun setupDrawer() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)

        loadFragment(DashboardFragment())

        navView.setNavigationItemSelectedListener {item -> handleNavViewClick(item)  }
    }

    fun loadFragment(fragment: BaseFragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.llContainer, fragment, fragment.javaClass.simpleName)
        transaction.addToBackStack(null)
        transaction.commit()

        drawerLayout.closeDrawers()
    }

    private fun handleNavViewClick(item: MenuItem): Boolean {

        when(item.itemId) {
            R.id.dashboard -> {
                loadFragment(DashboardFragment())
            }

            R.id.profile -> {
                loadFragment(ProfileFragment())
            }

            R.id.settings -> {
                loadFragment(SettingsFragment())
            }

            R.id.children -> {
                loadFragment(ChildrenFragment.newInstance(this.user!!))
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
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers()
        } else if(supportFragmentManager.backStackEntryCount > 0){
            val currentFragment = supportFragmentManager.findFragmentByTag(ChildDetailsFragment::class.java.simpleName)
            if(currentFragment != null && currentFragment.isVisible) {
                supportFragmentManager.popBackStackImmediate()
            }
        }
    }

    override fun handleResource(resource: Resource<List<UserChildrenJoin>>?) {
        resource?.let {
            when(it.status) {
                Status.SUCCESS -> {
                    this.user = it.data!![0]
                }
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
