package com.marijannovak.autismhelper.common.fragments

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.marijannovak.autismhelper.R

class LoadingDialog : DialogFragment() {

    private var tvLoadingMessage: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.TransparentDialog)
        this.isCancelable = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.dialog_loading, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvLoadingMessage = view.findViewById(R.id.tvLoadingMessage)
    }

    fun showWithMessage(supportFragmentManager: FragmentManager, message: String?) {
        message?.let {
            tvLoadingMessage?.text = it
        }
        this.show(supportFragmentManager, null)
    }

    fun updateMessage(message: String?) {
        message?.let {
            tvLoadingMessage?.text = it
        }
    }
}