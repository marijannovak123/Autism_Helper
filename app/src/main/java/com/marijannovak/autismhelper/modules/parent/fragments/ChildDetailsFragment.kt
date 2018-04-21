package com.marijannovak.autismhelper.modules.parent.fragments


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseFragment
import com.marijannovak.autismhelper.data.models.Child
import kotlinx.android.synthetic.main.fragment_child_details.*

class ChildDetailsFragment : BaseFragment() {

    private var child: Child? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        arguments?.let {
            child =  it.getSerializable(KEY_CHILD) as Child
        }
        return inflater.inflate(R.layout.fragment_child_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        child?.let {
            (activity as AppCompatActivity).supportActionBar?.title = it.name
            tvChildName.text = it.name
        }
    }

    companion object {
        private const val KEY_CHILD = "child"

        @JvmStatic
        fun newInstance(child: Child): ChildDetailsFragment {
            return ChildDetailsFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(KEY_CHILD, child)
                }
            }
        }
    }



}
