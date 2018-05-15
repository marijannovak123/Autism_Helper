package com.marijannovak.autismhelper.modules.parent.fragments


import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseFragment
import com.marijannovak.autismhelper.modules.parent.ParentActivity

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_settings, rootKey)

        (activity as ParentActivity).supportActionBar?.title = getString(R.string.settings)
    }

    //override fun onCreate(savedInstanceState: Bundle?) {
    //    super.onCreate(savedInstanceState)
    //    addPreferencesFromResource(R.xml.fragment_settings)
    //}

    //TODO: ASK FOR PASSWORD OR SOMETHING, voice for aac

}
