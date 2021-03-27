package com.mohamed.medhat.graduation_project.networking

import android.util.Log
import com.google.gson.Gson
import com.mohamed.medhat.graduation_project.model.Time
import com.mohamed.medhat.graduation_project.model.Token
import com.mohamed.medhat.graduation_project.utils.managers.TokenManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

private const val INTERCEPTOR_TAG = "INTERCEPTOR"

/**
 * A class that is responsible for intercepting all the requests to and the responses from the RESTFUL api.
 */
class NetworkInterceptor @Inject constructor(val tokenManager: TokenManager) : Interceptor {

    // TODO update this list whenever a new endpoint is added to [WebApi] interface
    private val nonAuthEndpoints = listOf(
        "${BASE_URL}Accounts/Mentor/Register",
        "${BASE_URL}Accounts/Login"
    )


    // Check https://medium.com/tiendeo-tech/android-refreshing-token-proactively-with-okhttp-interceptors-e7b90c47f5e7
    // for reference.
    override fun intercept(chain: Interceptor.Chain): Response {
        val rawRequest = chain.request()
        if (requiresAuth(rawRequest)) {
            // For requests that REQUIRE authentication
            Log.d(
                INTERCEPTOR_TAG,
                "Authentication is required for the endpoint \"${rawRequest.url}\""
            )
            if (tokenManager.getToken().isEmpty()) {
                // If no token was found, proceed without the token to get a 401 error.
                Log.e(INTERCEPTOR_TAG, "Couldn't find a saved token!")
                return chain.proceed(rawRequest)
            }
            val currentTime = getTime(chain, rawRequest)
            if (currentTime != null) {
                if (!tokenManager.isTokenExpired(currentTime.current)) {
                    // Token is valid
                    // Add headers
                    val newRequest = rawRequest.newBuilder()
                        .addHeader("Authorization", "Bearer ${tokenManager.getToken()}")
                        .build()
                    return chain.proceed(newRequest)
//---------------------------------------------------------------------------------------------------------------
                } else {
                    // Token expired
                    // Refresh token
                    Log.e(INTERCEPTOR_TAG, "Token Expired!")
                    return refreshToken(chain, rawRequest)
                }
            } else {
                Log.e(INTERCEPTOR_TAG, "Couldn't fetch the current time from the server!")
                // Unexpected error because we were unable to get the time.
                // Just proceed with the new request with the headers.
                val newRequest = rawRequest.newBuilder()
                    .addHeader("Authorization", "Bearer ${tokenManager.getToken()}")
                    .build()
                val response = chain.proceed(newRequest)
                if (response.code == 401) {
                    // Unauthorized
                    // Refresh token
                    return refreshToken(chain, rawRequest)
                } else {
                    // Success
                    return response
                }
            }
        } else {
            // For requests that don't require authentication, proceed with the original request
            // without adding any token to the header.
            Log.d(
                INTERCEPTOR_TAG,
                "No authentication required for the endpoint \"${rawRequest.url}\""
            )
            return chain.proceed(rawRequest)
        }
    }

    /**
     * A helper function that checks whether a request requires authentication using a token or not.
     * @param request the [Request] object under the check.
     * @return `true` if the request requires authentication, `false` otherwise.
     */
    private fun requiresAuth(request: Request): Boolean {
        val url = request.url.toString()
        nonAuthEndpoints.forEach {
            if (url == it) {
                return false
            }
        }
        return true
    }

    /**
     * @param chain an okHttp chain object.
     * @param request the request to override.
     * @return a [Time] object containing the current time.
     */
    private fun getTime(chain: Interceptor.Chain, request: Request): Time? {
        val timeRequest = request.newBuilder().method("get", null).url("${BASE_URL}Time").build()
        val response = chain.proceed(timeRequest)
        if (response.isSuccessful) {
            return try {
                Gson().fromJson(response.peekBody(1024).string(), Time::class.java)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
        return null
    }

    /**
     * @param chain an okHttp chain object.
     * @param request the request to override.
     * @return a [Response] object.
     */
    private fun refreshToken(chain: Interceptor.Chain, request: Request): Response {
        val refreshRequest = request.newBuilder()
            .method("post", null)
            .url("${BASE_URL}Accounts/RefreshToken/${tokenManager.getRefreshToken()}")
            .addHeader("Authorization", "Bearer ${tokenManager.getToken()}")
            .build()
        val response = chain.proceed(refreshRequest)
        if (response.isSuccessful) {
            try {
                val token =
                    Gson().fromJson(response.peekBody(1024).string(), Token::class.java)
                tokenManager.save(token)
                Log.d(INTERCEPTOR_TAG, "Token Refreshed!")
                // TODO resend the original request with the refreshed token
                // resending the original request (that requires authentication) with the new token.
                val requestToResend = request.newBuilder()
                    .addHeader("Authorization", "Bearer ${tokenManager.getToken()}")
                    .build()
                return chain.proceed(requestToResend)
            } catch (e: Exception) {
                e.printStackTrace()
                tokenManager.clearToken()
            }
        } else {
            if (response.code == 401) {
                // clear the token
                tokenManager.clearToken()
                Log.e(INTERCEPTOR_TAG, "Couldn't refresh the token!")
            }
        }
        return response
    }
}