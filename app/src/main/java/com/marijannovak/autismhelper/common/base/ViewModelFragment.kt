package com.marijannovak.autismhelper.common.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.modules.parent.fragments.ChildrenFragment
import com.marijannovak.autismhelper.modules.parent.fragments.DashboardFragment
import com.marijannovak.autismhelper.modules.parent.fragments.ProfileFragment
import com.marijannovak.autismhelper.modules.parent.fragments.SettingsFragment

open class ViewModelFragment : Fragment() {

    //todo: viewmodels

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as AppCompatActivity
        activity.supportActionBar?.title = getTitle()
    }

    private fun getTitle() : String {
        return when(this) {
            is DashboardFragment -> {
                getString(R.string.dashboard)
            }

            is ProfileFragment -> {
                getString(R.string.profile)
            }

            is SettingsFragment -> {
                getString(R.string.settings)
            }

            is ChildrenFragment -> {
                getString(R.string.children)
            }

            else -> ""
        }
    }
}