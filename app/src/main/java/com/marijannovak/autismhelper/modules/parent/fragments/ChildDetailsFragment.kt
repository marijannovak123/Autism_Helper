package com.marijannovak.autismhelper.modules.parent.fragments


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseFragment
import com.marijannovak.autismhelper.common.enums.Status
import com.marijannovak.autismhelper.data.models.Child
import com.marijannovak.autismhelper.data.models.ChildScore
import com.marijannovak.autismhelper.modules.parent.ParentActivity
import com.marijannovak.autismhelper.modules.parent.mvvm.ParentViewModel
import com.marijannovak.autismhelper.utils.Resource
import com.marijannovak.autismhelper.utils.toDateString
import kotlinx.android.synthetic.main.fragment_child_details.*

class ChildDetailsFragment : BaseFragment() {

    private var child: Child? = null
    private lateinit var parentViewModel: ParentViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        arguments?.let {
            child =  it.getSerializable(KEY_CHILD) as Child
        }
        return inflater.inflate(R.layout.fragment_child_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let {
            parentViewModel = ViewModelProviders.of(it).get(ParentViewModel::class.java)
            parentViewModel.chartLiveData.observe(this,
                    Observer {
                        handleResource(it)
                    })
        }
        child?.let {
            (activity as AppCompatActivity).supportActionBar?.title = it.name
            parentViewModel.loadChildScores(it.id)
        }
    }

    private fun handleResource(it: Resource<List<LineData>>?) {
        it?.let {
            (activity as ParentActivity).showLoading(it.status)
            when(it.status) {
                Status.SUCCESS -> {
                    setupChartData(it.data!![0])
                }

                else -> {

                }
            }
        }
    }

    private fun setupChartData(lineData: LineData) {
        lcTimeScores.data = lineData
        val xAxis = lcTimeScores.xAxis
        xAxis.valueFormatter = IAxisValueFormatter { value, _ ->
             value.toLong().toDateString()
        }
        lcTimeScores.invalidate()
    }

    private fun showBiggerChart(childScore: ChildScore) {
        //todo: dialog fragment
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
