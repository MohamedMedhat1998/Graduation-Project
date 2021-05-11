package com.mohamed.medhat.sanad.networking

import com.mohamed.medhat.sanad.model.*
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
     * A multipart post request that is used to register a new blind in the website database
     * based on the QR code.
     */
    @Multipart
    @POST("/Api/Mentority/RegisterBlind")
    suspend fun addBlind(
        @Part firstName: MultipartBody.Part,
        @Part lastName: MultipartBody.Part,
        @Part gender: MultipartBody.Part,
        @Part age: MultipartBody.Part,
        @Part phoneNumber: MultipartBody.Part,
        @Part serialNumber: MultipartBody.Part,
        @Part emergencyPhoneNumber: MultipartBody.Part,
        @Part bloodType: MultipartBody.Part,
        @Part("illnesses") illnesses: List<String>,
        @Part profilePicture: MultipartBody.Part
    ): Response<Any>

    /**
     * A get request that fetches the blind profiles mentored by the currently logged in mentor.
     */
    @GET("/Api/Mentority/Blinds")
    suspend fun getBlinds(): Response<List<BlindMiniProfile>>

    /**
     * A get function that returns the blind's last known location on the map.
     * @param serialNumber Which blind to get his/her location.
     */
    @GET("/Api/GPS/LastNode/{serialNumber}")
    suspend fun getLastNode(@Path("serialNumber") serialNumber: String): Response<GpsNode>

    /**
     * @param blindUsername Which blind to fetch his/her [KnownPerson]s.
     * @return A list of the [KnownPerson]s of the blind.
     */
    @GET("/Api/BlindKnownPersons/{blindUsername}")
    suspend fun getKnownPersons(@Path("blindUsername") blindUsername: String): Response<List<KnownPerson>>
}