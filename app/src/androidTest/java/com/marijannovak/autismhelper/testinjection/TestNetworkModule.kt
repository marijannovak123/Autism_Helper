package com.marijannovak.autismhelper.testinjection

import com.marijannovak.autismhelper.data.network.API
import com.marijannovak.autismhelper.di.NetworkModule
import org.mockito.Mockito
import retrofit2.Retrofit

class TestNetworkModule: NetworkModule() {
    override fun provideJsonApi(retrofit: Retrofit): API {
        return Mockito.mock(API::class.java)
    }

    override fun provideXmlApi(retrofit: Retrofit): API {
        return Mockito.mock(API::class.java)
    }
}