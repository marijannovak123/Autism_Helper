package com.marijannovak.autismhelper.util

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.marijannovak.autismhelper.App
import com.marijannovak.autismhelper.testinjection.TestAppComponent
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
abstract class BaseInstrumentedTest {

    private lateinit var testAppComponent: TestAppComponent

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        val app = InstrumentationRegistry.getTargetContext().applicationContext as App
        testAppComponent = DaggerTestAppComponent.builder()
                .application(app)
                .build()
        app.appComponent = testAppComponent
        testAppComponent.inject(this)
        before()
    }

    abstract fun before()
}