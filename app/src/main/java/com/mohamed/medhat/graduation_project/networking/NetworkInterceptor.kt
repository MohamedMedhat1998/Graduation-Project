package com.mohamed.medhat.graduation_project.networking

import com.mohamed.medhat.graduation_project.utils.managers.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * A class that is responsible for intercepting all the requests to and the responses from the RESTFUL api.
 */
class NetworkInterceptor @Inject constructor(val tokenManager: TokenManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val token = tokenManager.getToken()
        if (token.isNotEmpty()) {
            val newRequest =
                request.newBuilder().addHeader("Authorization", "Bearer $token")
                    .build()
            return chain.proceed(newRequest)
        }
        //Log.d("Request URL", request.url.toString())
        //Log.d("Response Code", response.code.toString())
        //Log.d("Response", response.peekBody(1024).string())
        return chain.proceed(request)
    }
}