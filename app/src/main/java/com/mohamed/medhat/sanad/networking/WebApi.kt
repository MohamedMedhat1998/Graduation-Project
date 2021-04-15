package com.mohamed.medhat.sanad.networking

import com.mohamed.medhat.sanad.model.LoginUser
import com.mohamed.medhat.sanad.model.MentorProfile
import com.mohamed.medhat.sanad.model.NewUser
import com.mohamed.medhat.sanad.model.Token
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

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

    /**
     * @return the mentor profile response body.
     */
    @GET("/Api/Accounts/Profile")
    suspend fun getMentorProfile(): Response<MentorProfile>

    /**
     * TODO
     */
    @Multipart
    @POST("/Api/Mentority/RegisterBlind")
    suspend fun addBlind(
        @Part("FirstName") firstName: String,
        @Part("LastName") lastName: String,
        @Part("Gender") gender: Int,
        @Part("Age") age: Int,
        @Part("PhoneNumber") phoneNumber: String,
        @Part("SerialNumber") serialNumber: String,
        @Part("EmergencyPhoneNumber") emergencyPhoneNumber: String,
        @Part("BloodType") bloodType: String,
        @Part("Illnesses") illnesses: List<String>,
        @Part profilePicture: MultipartBody.Part
    ): Response<Any> // TODO change the return type 'Any' to a suitable model
}