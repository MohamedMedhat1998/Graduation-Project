package com.mohamed.medhat.graduation_project.networking

import android.util.Log
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.Response

/**
 * A class that is responsible for intercepting all the requests to and the responses from the RESTFUL api.
 */
class NetworkInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        Log.d("Request URL", request.url.toString())
        val response = chain.proceed(request)
        Log.d("Response Code", response.code.toString())
        Log.d("Response", response.peekBody(1024).string())
        return response
    }
}