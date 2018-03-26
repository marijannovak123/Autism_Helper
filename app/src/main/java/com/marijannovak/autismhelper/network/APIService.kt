package com.marijannovak.autismhelper.network

import com.marijannovak.autismhelper.App.Companion.FIREBASE_API_KEY
import com.marijannovak.autismhelper.config.Constants
import com.marijannovak.autismhelper.config.Constants.Companion.FIREBASE_AUTH
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Marijan on 23.3.2018..
 */
class APIService private constructor(){

    companion object {

        var retrofit : Retrofit? = null

        fun getApi() : API {
            return getRetrofitService().create(API::class.java)
        }

        fun getRetrofitService() : Retrofit {
            if(retrofit == null) {
                retrofit = Retrofit.Builder()
                        .baseUrl(Constants.BASE_URL)
                        .client(getHttpClient())
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build()
            }

            return retrofit!!
        }

        fun getHttpClient() : OkHttpClient {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            return OkHttpClient.Builder()
                    .addInterceptor(httpLoggingInterceptor)
                    .addInterceptor { chain ->
                        val original = chain.request()
                        val httpUrl = original!!.url()

                        val newHttpUrl = httpUrl.newBuilder().addQueryParameter(FIREBASE_AUTH, FIREBASE_API_KEY).build()
                        val requestBuilder = original.newBuilder().url(newHttpUrl)
                        val request = requestBuilder.build()
                        chain.proceed(request)
                    }.build()
        }
    }
}