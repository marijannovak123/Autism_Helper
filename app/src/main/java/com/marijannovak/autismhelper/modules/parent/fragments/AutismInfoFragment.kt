package com.marijannovak.autismhelper.modules.parent.fragments


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient

import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_autism_info.*

class AutismInfoFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_autism_info, container, false)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        wvAutismInfo.settings.javaScriptEnabled = true
        wvAutismInfo.webViewClient = WebViewClient()

        wvAutismInfo.loadUrl("https://www.autismspeaks.org/what-autism")
    }
}
