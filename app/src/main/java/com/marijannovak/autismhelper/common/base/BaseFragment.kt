package com.marijannovak.autismhelper.common.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.modules.parent.fragments.ChildrenFragment
import com.marijannovak.autismhelper.modules.parent.fragments.ProfileFragment
import com.marijannovak.autismhelper.modules.parent.fragments.SettingsFragment

open class BaseFragment : Fragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = getTitle()
    }

    private fun getTitle() : String {
        return when(this) {
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