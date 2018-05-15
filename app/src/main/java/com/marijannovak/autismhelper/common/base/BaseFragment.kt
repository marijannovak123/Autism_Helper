package com.marijannovak.autismhelper.common.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.modules.parent.fragments.ChildrenFragment
import com.marijannovak.autismhelper.modules.parent.fragments.PhrasesFragment
import com.marijannovak.autismhelper.modules.parent.fragments.ProfileFragment
import com.marijannovak.autismhelper.modules.parent.fragments.SettingsFragment

//Pass activity class to get that activity's viewmodel
open class BaseFragment : Fragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = getTitle()
    }

    private fun getTitle(): String {
        return when (this) {
            is ProfileFragment -> {
                getString(R.string.profile)
            }

            is ChildrenFragment -> {
                getString(R.string.children)
            }

            is PhrasesFragment -> {
                getString(R.string.phrases)
            }

            else -> ""
        }
    }
}