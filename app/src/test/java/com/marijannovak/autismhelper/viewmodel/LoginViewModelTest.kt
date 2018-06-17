package com.marijannovak.autismhelper.viewmodel

import android.arch.lifecycle.Observer
import com.marijannovak.autismhelper.data.models.User
import com.marijannovak.autismhelper.data.repo.DataRepository
import com.marijannovak.autismhelper.modules.login.mvvm.LoginRepository
import com.marijannovak.autismhelper.modules.login.mvvm.LoginViewModel
import com.marijannovak.autismhelper.util.BaseUnitTest
import com.marijannovak.autismhelper.utils.Resource
import com.marijannovak.autismhelper.utils.TestDataGenerator
import io.reactivex.Completable
import io.reactivex.Maybe
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when` as whenever

class LoginViewModelTest: BaseUnitTest() {

    @Mock private lateinit var loginRepo: LoginRepository
    @Mock private lateinit var dataRepo: DataRepository
    @Mock private lateinit var observer: Observer<Resource<List<User>>>

    private lateinit var loginViewModel: LoginViewModel
    private val user: User = TestDataGenerator.testUser()

    override fun before() {
        loginViewModel = LoginViewModel(loginRepo, dataRepo)
        loginViewModel.resourceLiveData.observeForever(observer)
    }

    @Test
    fun loginCheckSuccessTest() {
        whenever(loginRepo.isLoggedIn()).thenReturn(Maybe.just(user))
        whenever(dataRepo.syncUserData()).thenReturn(Completable.complete())

        loginViewModel.checkLoggedIn()

        verify(observer).onChanged(Resource.loading())
        verify(observer).onChanged(Resource.success(listOf(user)))
    }
}