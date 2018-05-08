package com.marijannovak.autismhelper.modules.child

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.ViewModelActivity
import com.marijannovak.autismhelper.common.enums.Status
import com.marijannovak.autismhelper.data.models.AacPhrase
import com.marijannovak.autismhelper.modules.child.adapters.AACAdapter
import com.marijannovak.autismhelper.modules.child.mvvm.AACViewModel
import com.marijannovak.autismhelper.utils.Resource
import kotlinx.android.synthetic.main.activity_aac.*

class AACActivity : ViewModelActivity<AACViewModel, AacPhrase>() {

    private var aacSelectorAdapter: AACAdapter? = null
    private var aacDisplayAdapter: AACAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aac)

        viewModel.loadPhrases()
    }

    override fun handleResource(resource: Resource<List<AacPhrase>>?) {
        resource?.let {
            showLoading(it.status)
            when(it.status) {
                Status.SUCCESS -> {
                    setUpAacData(it.data)
                }

                else -> {

                }
            }
        }
    }

    private fun setUpAacData(phrases: List<AacPhrase>?) {
        if(aacDisplayAdapter == null) {
            aacDisplayAdapter = AACAdapter(emptyList(), {
                aacPhrase -> aacDisplayAdapter?.deleteItem(aacPhrase)
            })
            rvAacDisplay.adapter = aacDisplayAdapter
            rvAacDisplay.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        }

        phrases?.let {
            if(aacSelectorAdapter == null) {
                aacSelectorAdapter = AACAdapter(emptyList(), {
                    aacDisplayAdapter?.addItem(it)
                })
                rvAacSelector.adapter = aacSelectorAdapter
                rvAacSelector.layoutManager = GridLayoutManager(this, 3)
            }

            aacSelectorAdapter!!.update(it)
        }
    }

    override fun subscribeToData() {
        viewModel.resourceLiveData.observe(this, Observer{ handleResource(it) })
    }
}
