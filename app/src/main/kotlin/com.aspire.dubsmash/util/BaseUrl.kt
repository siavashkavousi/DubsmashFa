package com.aspire.dubsmash.util

import com.squareup.okhttp.HttpUrl
import retrofit.BaseUrl

/**
 * Created by sia on 11/18/15.
 */
class BaseUrl(var baseUrl: String) : BaseUrl {
    override fun url(): HttpUrl {
        return HttpUrl.parse(baseUrl) ?: throw IllegalArgumentException("Illegal URL: " + baseUrl)
    }
}