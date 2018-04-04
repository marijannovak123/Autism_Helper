package com.marijannovak.autismhelper.modules.login

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.listeners.LoginSignupListener

class LoginFragment : Fragment() {

    private var listener: LoginSignupListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is LoginSignupListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement LoginSignupListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }
}
