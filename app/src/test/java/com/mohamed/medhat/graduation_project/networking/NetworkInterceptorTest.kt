package com.mohamed.medhat.graduation_project.networking

import com.mohamed.medhat.graduation_project.utils.managers.TokenManager
import okhttp3.Request
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

/**
 * A test class that tests [NetworkInterceptor] class.
 */
class NetworkInterceptorTest {

    private lateinit var networkInterceptor: NetworkInterceptor
    private lateinit var tokenManager: TokenManager

    @Before
    fun setup() {
        tokenManager = mock(TokenManager::class.java)
        networkInterceptor = NetworkInterceptor(tokenManager)
    }

    @Test
    fun test_requiresAuth_fun() {
        val loginUrl = "${BASE_URL}Accounts/Login"
        val loginRequest = Request.Builder().url(loginUrl).build()
        assert(!networkInterceptor.requiresAuth(loginRequest))
        val confirmationUrl = "${BASE_URL}Accounts/ConfirmEmail/{confirmationCode}"
        val confirmationRequest = loginRequest.newBuilder().url(confirmationUrl).build()
        assert(networkInterceptor.requiresAuth(confirmationRequest))
    }
}