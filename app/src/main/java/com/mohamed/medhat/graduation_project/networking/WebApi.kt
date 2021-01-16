package com.mohamed.medhat.graduation_project.networking

import com.mohamed.medhat.graduation_project.model.NewUser
import com.mohamed.medhat.graduation_project.model.Token
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Contains all the endpoints from the web service. This class returns real data from the remote RESTFUL web service.
 */
interface WebApi {
    /**
     * Used to register a new user to the remote database.
     * @param newUser: The new user object that contains the new user details.
     * @return a [Token] object.
     */
    @POST("Accounts/Mentor/Register")
    suspend fun register(@Body newUser: NewUser): Response<Token>
}