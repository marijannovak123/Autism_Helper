package com.marijannovak.autismhelper.ui

import com.marijannovak.autismhelper.BuildConfig
import com.marijannovak.autismhelper.modules.login.LoginActivity
import com.marijannovak.autismhelper.testinjection.TestApp
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(constants = BuildConfig::class, sdk = [21], application = TestApp::class)
@RunWith(RobolectricTestRunner::class)
class LoginActivityTest {
    @Test
    fun test() {
        val loginActivity = Robolectric.buildActivity(LoginActivity::class.java).create().visible().get()

    }
}