package com.marijannovak.autismhelper.ui.activities

import androidx.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.core.view.GravityCompat
import android.view.MenuItem
import android.view.View
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseFragment
import com.marijannovak.autismhelper.common.base.ViewModelActivity
import com.marijannovak.autismhelper.common.enums.Status
import com.marijannovak.autismhelper.config.Constants.Companion.FRAGMENT_CHILDREN
import com.marijannovak.autismhelper.config.Constants.Companion.FRAGMENT_INFO
import com.marijannovak.autismhelper.config.Constants.Companion.FRAGMENT_PHRASES
import com.marijannovak.autismhelper.config.Constants.Companion.FRAGMENT_PROFILE
import com.marijannovak.autismhelper.config.Constants.Companion.FRAGMENT_RSS
import com.marijannovak.autismhelper.data.models.UserChildrenJoin
import com.marijannovak.autismhelper.viewmodels.ParentViewModel
import com.marijannovak.autismhelper.ui.fragments.*
import com.marijannovak.autismhelper.utils.DialogHelper
import com.marijannovak.autismhelper.utils.Resource
import kotlinx.android.synthetic.main.activity_parent.*
import kotlinx.android.synthetic.main.nav_header.view.*

class ParentActivity : ViewModelActivity<ParentViewModel, UserChildrenJoin>() {

    private lateinit var fragments: Map<String, BaseFragment>
    private lateinit var drawerAction: () -> Unit
    private var handler = Handler()
    private var fragmentLoad = false
    private var currentFragment = ChildrenFragment::class.java.simpleName

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
                Pair(FRAGMENT_PHRASES, PhrasesFragment()),
                Pair(FRAGMENT_RSS, RssFragment()),
                Pair(FRAGMENT_INFO, AutismInfoFragment())
        )
    }

    private fun setupDrawer() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)

        loadFragment(fragments[FRAGMENT_CHILDREN]!!)
        navView.menu.findItem(R.id.children).isChecked = true
        navView.setNavigationItemSelectedListener { item -> handleNavViewClick(item) }
        drawerLayout.addDrawerListener(object: androidx.drawerlayout.widget.DrawerLayout.DrawerListener {
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

    fun <T : BaseFragment> loadFragment(fragment: T) {
        currentFragment = fragment.javaClass.simpleName
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.llContainer, fragment, fragment.javaClass.simpleName)
        transaction.addToBackStack(null)
        transaction.commit()
    }



    private fun handleNavViewClick(item: MenuItem): Boolean {
        fragmentLoad = when(item.itemId) {
            R.id.profile, R.id.settings, R.id.phrases, R.id.children, R.id.rss, R.id.info -> true
            else -> false
        }

        when (item.itemId) {
            R.id.profile -> {
                drawerAction = { loadFragment(fragments[FRAGMENT_PROFILE]!!) }
            }

            R.id.rss -> {
                drawerAction = { loadFragment(fragments[FRAGMENT_RSS]!!) }
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

            R.id.info -> {
                drawerAction = { loadFragment(fragments[FRAGMENT_INFO]!!) }
            }

            R.id.sync -> {
                viewModel.syncUserAndData()
            }

            R.id.logout -> {
                DialogHelper.showPromptDialog(this, getString(R.string.really_logout)) {
                    viewModel.logOut()
                }
            }

            R.id.exit -> {
                DialogHelper.showPromptDialog(this, getString(R.string.really_exit)) {
                    startActivity(Intent(this@ParentActivity, MainActivity::class.java))
                    finish()
                }
            }
        }

        drawerLayout.closeDrawers()
        return true
    }

    override fun subscribeToData() {
        viewModel.resource.observe(this, Observer { resource -> handleResource(resource) })
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

    override fun handleResource(resource: Resource<UserChildrenJoin>?) {
        resource?.let {
            handleLoading(it.status, it.message)
            when (it.status) {
                Status.HOME -> {
                    val intent = Intent(this@ParentActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                Status.MESSAGE -> {
                    showMessage(0, it.message)
                }

                Status.SAVED -> {
                   showMessage(R.string.saved, null)
                    if(currentFragment == PhrasesFragment::class.java.simpleName) {
                        (fragments[FRAGMENT_PHRASES] as PhrasesFragment).showAddPhrase(false)
                    } else {
                        loadFragment(fragments[FRAGMENT_CHILDREN]!!)
                    }
                }

                else -> {

                }
            }
        }
    }

}
