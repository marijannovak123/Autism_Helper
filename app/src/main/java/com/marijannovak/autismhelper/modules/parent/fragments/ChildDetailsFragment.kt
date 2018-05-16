package com.marijannovak.autismhelper.modules.parent.fragments

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.InjectableFragment
import com.marijannovak.autismhelper.common.enums.Status
import com.marijannovak.autismhelper.data.models.Child
import com.marijannovak.autismhelper.data.models.ChildScore
import com.marijannovak.autismhelper.modules.parent.ParentActivity
import com.marijannovak.autismhelper.modules.parent.mvvm.ParentRepository
import com.marijannovak.autismhelper.modules.parent.mvvm.ParentViewModel
import com.marijannovak.autismhelper.utils.Resource
import com.marijannovak.autismhelper.utils.logTag
import kotlinx.android.synthetic.main.fragment_child_details.*

class ChildDetailsFragment : InjectableFragment<ParentViewModel>() {

    private var child: Child? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        arguments?.let {
            child = it.getSerializable(KEY_CHILD) as Child
        }
        return inflater.inflate(R.layout.fragment_child_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let {
            viewModel.chartLiveData.observe(this,
                    Observer {
                        setupChartData(it)
                    })

            child?.let {
                (activity as AppCompatActivity).supportActionBar?.title = it.name + getString(R.string.its_scores)
                viewModel.loadChildScores(it)
            }
        }
    }

    private fun setupChartData(chartData: ParentRepository.ChartData?) {
        chartData?.let {
            if (lcTimeScores.data == null && bcMistakes.data == null) {
                val dateFormatter = IAxisValueFormatter { value, _ ->
                    it.dates[value.toInt()]
                }

                lcTimeScores.xAxis.valueFormatter = dateFormatter
                lcTimeScores.description.isEnabled = false

                bcMistakes.xAxis.valueFormatter = dateFormatter
                bcMistakes.description.isEnabled = false
            }

            if (it.lineData.entryCount > 0 && it.barData.entryCount > 0) {
                lcTimeScores.data = it.lineData
                lcTimeScores.animateXY(0, 300)

                bcMistakes.data = it.barData
                bcMistakes.animateXY(0, 300)
            }
        }
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
