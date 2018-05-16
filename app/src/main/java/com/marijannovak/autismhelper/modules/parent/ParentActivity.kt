package com.marijannovak.autismhelper.modules.parent

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.view.MenuItem
import android.view.View
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseFragment
import com.marijannovak.autismhelper.common.base.ViewModelActivity
import com.marijannovak.autismhelper.common.enums.Status
import com.marijannovak.autismhelper.config.Constants.Companion.FRAGMENT_CHILDREN
import com.marijannovak.autismhelper.config.Constants.Companion.FRAGMENT_PHRASES
import com.marijannovak.autismhelper.config.Constants.Companion.FRAGMENT_PROFILE
import com.marijannovak.autismhelper.data.models.UserChildrenJoin
import com.marijannovak.autismhelper.modules.login.LoginActivity
import com.marijannovak.autismhelper.modules.main.MainActivity
import com.marijannovak.autismhelper.modules.parent.fragments.*
import com.marijannovak.autismhelper.modules.parent.mvvm.ParentViewModel
import com.marijannovak.autismhelper.utils.Resource
import kotlinx.android.synthetic.main.activity_parent.*
import kotlinx.android.synthetic.main.nav_header.view.*

class ParentActivity : ViewModelActivity<ParentViewModel, UserChildrenJoin>() {

    private lateinit var fragments: Map<String, BaseFragment>
    private lateinit var drawerAction: () -> Unit
    private var handler = Handler()
    private var fragmentLoad = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent)

        instantiateFragments()
        setupDrawer()
    }

    private fun instantiateFragments() {
        fragments = mutableMapOf(
                Pair(FRAGMENT_CHILDREN, ChildrenFragment()),
                Pair(FRAGMENT_PROFILE, ProfileFragment()),
                Pair(FRAGMENT_PHRASES, PhrasesFragment())
        )
    }

    private fun setupDrawer() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)

        loadFragment(fragments[FRAGMENT_CHILDREN]!!)
        navView.menu.findItem(R.id.children).isChecked = true
        navView.setNavigationItemSelectedListener { item -> handleNavViewClick(item) }
        drawerLayout.addDrawerListener(object: DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {
                navView.tvProfileName?.let {
                    if(it.text.isEmpty()) {
                        if(viewModel.userName.isEmpty()) {
                            viewModel.loadUsername()
                        } else {
                            it.text = viewModel.userName
                        }
                    }
                }
            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
               //NOOP
            }

            override fun onDrawerClosed(drawerView: View) {
                if(fragmentLoad) {
                    handler.postDelayed(drawerAction, 50)
                }
            }

            override fun onDrawerOpened(drawerView: View) {
            }
        })
    }

    fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.llContainer, fragment, fragment.javaClass.simpleName)
        transaction.addToBackStack(null)
        transaction.commit()
    }



    private fun handleNavViewClick(item: MenuItem): Boolean {
        fragmentLoad = when(item.itemId) {
            R.id.profile, R.id.settings, R.id.phrases, R.id.children -> true
            else -> false
        }

        when (item.itemId) {
            R.id.profile -> {
                drawerAction = { loadFragment(fragments[FRAGMENT_PROFILE]!!) }
            }

            R.id.settings -> {
                drawerAction = { loadFragment(SettingsFragment()) }
            }

            R.id.children -> {
                drawerAction = { loadFragment(fragments[FRAGMENT_CHILDREN]!!) }
            }

            R.id.phrases -> {
                drawerAction = { loadFragment(fragments[FRAGMENT_PHRASES]!!) }
            }

            R.id.sync -> {
                viewModel.syncUserAndData()
            }

            R.id.logout -> {
                viewModel.logOut()
            }

            R.id.exit -> {
                startActivity(Intent(this@ParentActivity, MainActivity::class.java))
                finish()
            }
        }

        drawerLayout.closeDrawers()
        return true
    }

    override fun subscribeToData() {
        viewModel.resourceLiveData.observe(this, Observer { resource -> handleResource(resource) })
        viewModel.userNameLiveData.observe(this, Observer { username -> navView.tvProfileName?.text = username })
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
            val childDetailsFragment = supportFragmentManager.findFragmentByTag(ChildDetailsFragment::class.java.simpleName)
            if (childDetailsFragment != null && childDetailsFragment.isVisible) {
                //to avoid crashes opening the details fragment with another child data, viewmodel first emmits the old data and then the app crashes
                //in formatter -> if bigger data set, out of bounds exception
                viewModel.chartLiveData.value = null
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
