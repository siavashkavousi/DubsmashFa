package com.aspire.dubsmash.siavash.util

import com.aspire.dubsmash.util.BaseUrl
import com.squareup.okhttp.OkHttpClient
import retrofit.GsonConverterFactory
import retrofit.Retrofit

/**
 * Created by sia on 11/18/15.
 */
object NetworkService {
    fun <S> createService(serviceClass: Class<S>, baseUrl: BaseUrl): S {
        return Retrofit.Builder().baseUrl(baseUrl).client(OkHttpClient()).addConverterFactory(GsonConverterFactory.create()).build().create(serviceClass)
    }
}