package com.marijannovak.autismhelper.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.marijannovak.autismhelper.config.Constants
import com.marijannovak.autismhelper.config.Constants.Companion.API_JSON
import com.marijannovak.autismhelper.config.Constants.Companion.API_XML
import com.marijannovak.autismhelper.config.Constants.Companion.BASE_URL
import com.marijannovak.autismhelper.config.Constants.Companion.RETROFIT_JSON
import com.marijannovak.autismhelper.config.Constants.Companion.RETROFIT_XML
import com.marijannovak.autismhelper.data.network.API
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
open class NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttp(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addInterceptor { chain ->
                    val original = chain.request()
                    val httpUrl = original!!.url()
                    val newHttpUrl = httpUrl.newBuilder().addQueryParameter(Constants.FIREBASE_AUTH, Constants.FIREBASE_API_KEY).build()
                    val requestBuilder = original.newBuilder().url(newHttpUrl)
                    val request = requestBuilder.build()
                    chain.proceed(request)
                }.build()
    }

    @Singleton
    @Provides
    @Named(RETROFIT_JSON)
    fun provideJsonRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    @Singleton
    @Provides
    @Named(RETROFIT_XML)
    fun provideXmlRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    @Singleton
    @Provides
    @Named(API_JSON)
    open fun provideJsonApi(@Named(RETROFIT_JSON) retrofit: Retrofit): API {
        return retrofit.create(API::class.java)
    }

    @Singleton
    @Provides
    @Named(API_XML)
    open fun provideXmlApi(@Named(RETROFIT_XML) retrofit: Retrofit): API {
        return retrofit.create(API::class.java)
    }

    @Singleton
    @Provides
    fun provideAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun provideStorage(): StorageReference {
        val storage = FirebaseStorage.getInstance()
        storage.maxDownloadRetryTimeMillis = 2000
        return storage.reference
    }
}