package com.marijannovak.autismhelper.util

import android.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@Suppress("UNCHECKED_CAST")
@RunWith(MockitoJUnitRunner::class)
abstract class BaseUnitTest {

    //mock architecture components background work
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        before()
    }

    protected abstract fun before()

    /**
     * fix for kotlin problem with Mockito any() - NPE
     * Use this one instead of Mockito's!
     */
    fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T
}