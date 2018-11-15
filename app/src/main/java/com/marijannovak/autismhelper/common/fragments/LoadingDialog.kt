package com.marijannovak.autismhelper.common.fragments

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.marijannovak.autismhelper.R

class LoadingDialog : androidx.fragment.app.DialogFragment() {

    private var tvLoadingMessage: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(androidx.fragment.app.DialogFragment.STYLE_NO_TITLE, R.style.TransparentDialog)
        this.isCancelable = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.dialog_loading, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvLoadingMessage = view.findViewById(R.id.tvLoadingMessage)
    }

    fun showWithMessage(supportFragmentManager: androidx.fragment.app.FragmentManager, message: String?) {
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