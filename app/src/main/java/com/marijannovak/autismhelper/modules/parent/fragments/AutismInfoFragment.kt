package com.marijannovak.autismhelper.modules.parent.fragments


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient

import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseFragment
import com.marijannovak.autismhelper.common.fragments.LoadingDialog
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
        wvAutismInfo.webChromeClient = object: WebChromeClient() {
            var progressDialog: LoadingDialog? = null
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                if(newProgress < 100 && progressDialog == null) {
                    progressDialog = LoadingDialog()
                    progressDialog!!.show(fragmentManager, "")
                } else if(newProgress == 100) {
                    progressDialog?.dismiss()
                    progressDialog = null
                }
            }


        }

        wvAutismInfo.webViewClient = object: WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }
        }

        wvAutismInfo.loadUrl("https://www.autismspeaks.org/what-autism")
    }
}
