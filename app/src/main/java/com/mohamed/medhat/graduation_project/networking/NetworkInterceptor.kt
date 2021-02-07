package com.mohamed.medhat.graduation_project.networking

import com.mohamed.medhat.graduation_project.utils.managers.TokenManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

/**
 * A class that is responsible for intercepting all the requests to and the responses from the RESTFUL api.
 */
class NetworkInterceptor @Inject constructor(val tokenManager: TokenManager) : Interceptor {

    // TODO update this list whenever a new endpoint is added to [WebApi] interface
    private val nonAuthEndpoints = listOf(
        "${BASE_URL}Accounts/Mentor/Register",
        "${BASE_URL}Accounts/Login"
    )

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        return if (requiresAuth(originalRequest)) {
            // For requests that REQUIRE authentication, proceed with a NEW REQUEST and add the
            // TOKEN to the header.
            val newRequest = originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer ${tokenManager.getToken()}")
                .build()
            chain.proceed(newRequest)
        } else {
            // For requests that don't require authentication, proceed with the original request
            // without adding any token to the header.
            chain.proceed(originalRequest)
        }
    }

    /**
     * A helper function that checks whether a request requires authentication using a token or not.
     * @param request the [Request] object under the check.
     * @return `true` if the request requires authentication, `false` otherwise.
     */
    fun requiresAuth(request: Request): Boolean {
        val url = request.url.toString()
        nonAuthEndpoints.forEach {
            if (url == it) {
                return false
            }
        }
        return true
    }
}