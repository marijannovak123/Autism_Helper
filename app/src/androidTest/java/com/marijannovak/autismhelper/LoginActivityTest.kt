package com.marijannovak.autismhelper

import android.arch.lifecycle.MutableLiveData
import android.support.test.rule.ActivityTestRule
import com.marijannovak.autismhelper.data.models.User
import com.marijannovak.autismhelper.modules.login.LoginActivity
import com.marijannovak.autismhelper.modules.login.mvvm.LoginViewModel
import com.marijannovak.autismhelper.util.BaseInstrumentedTest
import com.marijannovak.autismhelper.utils.Resource
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when` as whenever

class LoginActivityTest: BaseInstrumentedTest() {

    @get:Rule
    var loginActivityRule = ActivityTestRule<LoginActivity>(LoginActivity::class.java, true, true)

    @Mock
    private lateinit var loginViewModel: LoginViewModel

    private val resourceLiveData = MutableLiveData<Resource<List<User>>>()

    override fun before() {
        loginActivityRule.activity.viewModel = loginViewModel
    }

    @Test
    fun teyr() {
        Thread.sleep(1000)
    }
}