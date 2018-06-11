package com.marijannovak.autismhelper.modules.child.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.marijannovak.autismhelper.R

class PhraseCategoryFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_phrase_category, container, false)
    }


    companion object {
        @JvmStatic
        fun newInstance() =
                PhraseCategoryFragment().apply {
                    arguments = Bundle().apply {

                    }
                }
    }
}
