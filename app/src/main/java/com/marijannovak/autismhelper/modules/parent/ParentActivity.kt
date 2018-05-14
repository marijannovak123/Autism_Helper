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
import com.marijannovak.autismhelper.config.Constants.Companion.FRAGMENT_CHILDREN
import com.marijannovak.autismhelper.config.Constants.Companion.FRAGMENT_PHRASES
import com.marijannovak.autismhelper.config.Constants.Companion.FRAGMENT_PROFILE
import com.marijannovak.autismhelper.config.Constants.Companion.FRAGMENT_SETTINGS
import com.marijannovak.autismhelper.data.models.UserChildrenJoin
import com.marijannovak.autismhelper.modules.login.LoginActivity
import com.marijannovak.autismhelper.modules.main.MainActivity
import com.marijannovak.autismhelper.modules.parent.fragments.*
import com.marijannovak.autismhelper.modules.parent.mvvm.ParentViewModel
import com.marijannovak.autismhelper.utils.Resource
import kotlinx.android.synthetic.main.activity_parent.*
import kotlinx.android.synthetic.main.nav_header.*

class ParentActivity : ViewModelActivity<ParentViewModel, UserChildrenJoin>() {

    private lateinit var fragments: Map<String, BaseFragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent)

        instantiateFragments()
        viewModel.loadUserWithChildren()
    }

    private fun instantiateFragments() {
        fragments = mutableMapOf(
                Pair(FRAGMENT_CHILDREN, ChildrenFragment()),
                Pair(FRAGMENT_PROFILE, ProfileFragment()),
                Pair(FRAGMENT_PHRASES, PhrasesFragment()),
                Pair(FRAGMENT_SETTINGS, SettingsFragment())
        )
    }

    private fun setupDrawer(userWithChildren: UserChildrenJoin) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)

        loadFragment(fragments[FRAGMENT_PROFILE]!!)
        navView.menu.findItem(R.id.profile).isChecked = true
        navView.setNavigationItemSelectedListener { item -> handleNavViewClick(item) }

        with(userWithChildren.user) {
            tvProfileName.text = username
        }
    }

    fun loadFragment(fragment: BaseFragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.llContainer, fragment, fragment.javaClass.simpleName)
        transaction.addToBackStack(null)
        transaction.commit()

        drawerLayout.closeDrawers()
    }

    private fun handleNavViewClick(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile -> {
                loadFragment(fragments[FRAGMENT_PROFILE]!!)
            }

            R.id.settings -> {
                loadFragment(fragments[FRAGMENT_SETTINGS]!!)
            }

            R.id.children -> {
                loadFragment(fragments[FRAGMENT_CHILDREN]!!)
            }

            R.id.phrases -> {
                loadFragment(fragments[FRAGMENT_PHRASES]!!)
            }

            R.id.sync -> {
                viewModel.syncData(false)
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
            when (item.itemId) {
                android.R.id.home -> {
                    drawerLayout.openDrawer(GravityCompat.START)
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {

            drawerLayout.closeDrawers()
        } else if (supportFragmentManager.backStackEntryCount > 0) {
            val currentFragment = supportFragmentManager.findFragmentByTag(ChildDetailsFragment::class.java.simpleName)
            if (currentFragment != null && currentFragment.isVisible) {
                loadFragment(ChildrenFragment())
            } else if (fragments[FRAGMENT_PHRASES]!!.isVisible) {
                val phrasesFragment = fragments[FRAGMENT_PHRASES]!! as PhrasesFragment
                if (phrasesFragment.isAddPhraseShown()) {
                    phrasesFragment.showAddPhrase(false)
                }
            }
        }
    }

    override fun handleResource(resource: Resource<List<UserChildrenJoin>>?) {
        resource?.let {
            showLoading(it.status)
            when (it.status) {
                Status.SUCCESS -> {
                    setupDrawer(it.data!![0])
                }

                Status.HOME -> {
                    val intent = Intent(this@ParentActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                Status.MESSAGE -> {
                    showError(0, it.message)
                }
                else -> {

                }
            }
        }
    }

}
