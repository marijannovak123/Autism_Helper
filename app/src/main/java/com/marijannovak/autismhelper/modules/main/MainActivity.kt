package com.marijannovak.autismhelper.modules.main

import android.os.Bundle
import com.marijannovak.autismhelper.R
import com.marijannovak.autismhelper.common.base.ViewModelActivity
import com.marijannovak.autismhelper.modules.main.mvvm.MainRepository
import com.marijannovak.autismhelper.modules.main.mvvm.MainViewModel

class MainActivity : ViewModelActivity<MainViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun createViewModel(): MainViewModel {
        return MainViewModel(MainRepository())
    }

    override fun subscribeToData() {

    }
}
