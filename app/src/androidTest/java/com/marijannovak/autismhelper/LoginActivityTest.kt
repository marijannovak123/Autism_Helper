package com.marijannovak.autismhelper

import android.arch.lifecycle.MutableLiveData
import android.support.test.rule.ActivityTestRule
import com.marijannovak.autismhelper.data.models.User
import com.marijannovak.autismhelper.modules.login.LoginActivity
import com.marijannovak.autismhelper.modules.login.mvvm.LoginViewModel
import com.marijannovak.autismhelper.util.BaseInstrumentedTest
import com.marijannovak.autismhelper.utils.Resource
import com.marijannovak.autismhelper.utils.TestDataGenerator
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when` as whenever

class LoginActivityTest: BaseInstrumentedTest() {

    @get:Rule
    var loginActivityRule = ActivityTestRule<LoginActivity>(LoginActivity::class.java, false, false)

    @Mock
    private lateinit var loginViewModel: LoginViewModel

    private var resourceLiveData = MutableLiveData<Resource<List<User>>>()
    private val users: List<User>? = listOf(TestDataGenerator.testUser())

    override fun before() {
        loginActivityRule.activity.viewModel = loginViewModel
    }


    @Test
    fun checkLoggedInTest() {
        whenever(loginViewModel.resourceLiveData).thenReturn(resourceLiveData)
        whenever(loginViewModel.checkLoggedIn()).then { this.resourceLiveData.postValue(Resource.success(users))}

        loginActivityRule.launchActivity(null)
    }
}