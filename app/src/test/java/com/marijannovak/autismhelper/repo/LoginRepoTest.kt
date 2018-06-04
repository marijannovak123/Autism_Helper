package com.marijannovak.autismhelper.repo

import com.google.firebase.auth.FirebaseAuth
import com.marijannovak.autismhelper.data.database.AppDatabase
import com.marijannovak.autismhelper.data.database.dao.UserDao
import com.marijannovak.autismhelper.data.models.User
import com.marijannovak.autismhelper.data.network.API
import com.marijannovak.autismhelper.modules.login.mvvm.LoginRepository
import com.marijannovak.autismhelper.util.BaseUnitTest
import com.marijannovak.autismhelper.util.TestDataGenerator
import com.marijannovak.autismhelper.utils.PrefsHelper
import io.reactivex.Maybe
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.TestScheduler
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when` as whenever

class LoginRepoTest: BaseUnitTest(){

    @Mock private lateinit var auth: FirebaseAuth
    @Mock private lateinit var api: API
    @Mock private lateinit var db: AppDatabase
    @Mock private lateinit var prefsHelper: PrefsHelper
    @Mock private lateinit var userDao: UserDao

    private var testScheduler = TestScheduler()
    private var testObserver = TestObserver<User>()
    private val user = TestDataGenerator.testUser()

    private lateinit var loginRepo: LoginRepository

    override fun before() {
        loginRepo = LoginRepository(auth, db, api, prefsHelper, testScheduler, testScheduler)
    }

    @Test
    fun dbReturnsUserLoggedInSuccess() {
        whenever(db.userDao()).thenReturn(userDao)
        whenever(db.userDao().userLoggedIn()).thenReturn(Maybe.just(user))

        loginRepo
                .isLoggedIn()
                .subscribe(testObserver)

        //do the threaded work
        testScheduler.triggerActions()

        //wait for scheduler to finish before asserting
        testObserver.awaitTerminalEvent()
        testObserver.assertValue(user)

    }
}