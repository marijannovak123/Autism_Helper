package com.marijannovak.autismhelper.modules.parent.fragments


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.BaseFragment
import com.marijannovak.autismhelper.common.enums.Status
import com.marijannovak.autismhelper.data.models.Child
import com.marijannovak.autismhelper.data.models.ChildScore
import com.marijannovak.autismhelper.modules.parent.ParentActivity
import com.marijannovak.autismhelper.modules.parent.mvvm.ParentRepository
import com.marijannovak.autismhelper.modules.parent.mvvm.ParentViewModel
import com.marijannovak.autismhelper.utils.Resource
import kotlinx.android.synthetic.main.fragment_child_details.*

class ChildDetailsFragment : BaseFragment() {

    private var child: Child? = null
    private lateinit var parentViewModel: ParentViewModel

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

    private fun handleResource(it: Resource<List<ParentRepository.ChartData>>?) {
        it?.let {
            (activity as ParentActivity).showLoading(it.status)
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let {
                        setupChartData(it[0])
                    }
                }

                else -> {

                }
            }
        }
    }

    //todo: coloring, style..
    private fun setupChartData(chartData: ParentRepository.ChartData) {
        if (lcTimeScores.data == null && bcMistakes.data == null) {
            val dateFormatter = IAxisValueFormatter { value, _ ->
                chartData.dates[value.toInt()]
            }

            lcTimeScores.xAxis.valueFormatter = dateFormatter
            lcTimeScores.description.isEnabled = false


            bcMistakes.xAxis.valueFormatter = dateFormatter
            bcMistakes.description.isEnabled = false

        }

        if (chartData.lineData.entryCount > 0 && chartData.barData.entryCount > 0) {
            lcTimeScores.data = chartData.lineData
            lcTimeScores.invalidate()

            bcMistakes.data = chartData.barData
            bcMistakes.invalidate()
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
