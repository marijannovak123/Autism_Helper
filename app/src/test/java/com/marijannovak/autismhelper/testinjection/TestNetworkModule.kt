package com.marijannovak.autismhelper.testinjection

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.StorageReference
import com.marijannovak.autismhelper.data.network.API
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import org.mockito.Mockito
import retrofit2.Retrofit

@Module
class TestNetworkModule {

    @Provides
    fun provideOkHttp(): OkHttpClient {
        return Mockito.mock(OkHttpClient::class.java)
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Mockito.mock(Retrofit::class.java)
    }


    @Provides
    fun provideApi(retrofit: Retrofit): API {
        return Mockito.mock(API::class.java)
    }

    @Provides
    fun provideAuth(): FirebaseAuth {
        return Mockito.mock(FirebaseAuth::class.java)
    }

    @Provides
    fun provideStorage(): StorageReference {
        return Mockito.mock(StorageReference::class.java)
    }
}