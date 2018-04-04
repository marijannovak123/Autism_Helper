package com.marijannovak.autismhelper.modules.login.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.marijannovak.autismhelper.modules.login.LoginFragment
import com.marijannovak.autismhelper.modules.login.SignupFragment

class LoginSignupPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    private var currentFragment : Fragment? = null

    override fun getItem(position: Int): Fragment {
        currentFragment = when(position) {
            0 -> LoginFragment.newInstance()
            else -> SignupFragment.newInstance()
        }

        return currentFragment as Fragment
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? =
            when(position) {
                0 -> "Login"
                else -> "Sign Up"
            }
}