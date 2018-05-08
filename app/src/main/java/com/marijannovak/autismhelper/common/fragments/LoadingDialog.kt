package com.marijannovak.autismhelper.common.fragments

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marijannovak.autismhelper.R

class LoadingDialog : DialogFragment() {
    //TODO: ADD WHAT'S HAPPENING
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.TransparentDialog)
        this.isCancelable = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
        = inflater.inflate(R.layout.dialog_loading, container, false)

}