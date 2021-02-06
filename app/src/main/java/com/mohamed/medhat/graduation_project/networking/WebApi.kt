package com.mohamed.medhat.graduation_project.networking

import com.mohamed.medhat.graduation_project.model.LoginUser
import com.mohamed.medhat.graduation_project.model.NewUser
import com.mohamed.medhat.graduation_project.model.Token
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

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

    /**
     * Used to login to an existing user in the remote database.
     * @param loginUser: The user to login to.
     * @return a [Token] object.
     */
    @POST("Accounts/Login")
    suspend fun login(@Body loginUser: LoginUser): Response<Token>

    /**
     * Sends the confirmation code to the remote api to confirm the email address.
     * @param confirmationCode the confirmation code received by email.
     * @return a string value = `true` if the confirmation was successful.
     */
    @POST("Accounts/ConfirmEmail/{confirmationCode}")
    suspend fun confirmEmail(@Path("confirmationCode") confirmationCode: String): Response<Any>
}