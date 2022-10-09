package com.example.rates.currency_rates.data.network

import com.example.rates.currency_rates.data.network.Constants.HEADER_API_KEY
import okhttp3.Interceptor
import okhttp3.Response

class SupportInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().addHeader(HEADER_API_KEY, Constants.apiKey).build()
        return chain.proceed(request)
    }
}