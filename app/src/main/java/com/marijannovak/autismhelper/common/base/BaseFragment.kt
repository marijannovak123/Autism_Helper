package com.marijannovak.autismhelper.common.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.ui.fragments.*

//Pass activity class to get that activity's viewmodel
open class BaseFragment : androidx.fragment.app.Fragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = getTitle()
    }

    private fun getTitle(): String {
        return when (this) {
            is ProfileFragment -> getString(R.string.profile)

            is ChildrenFragment -> getString(R.string.children)

            is SettingsFragment -> getString(R.string.settings)

            is RssFragment -> getString(R.string.news)

            is PhrasesFragment -> getString(R.string.phrases)

            is AutismInfoFragment -> getString(R.string.autism_info)

            else -> ""
        }
    }
}