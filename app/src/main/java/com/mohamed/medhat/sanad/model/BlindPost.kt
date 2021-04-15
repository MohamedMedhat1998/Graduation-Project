package com.mohamed.medhat.sanad.model

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.InputStream

/**
 * A data class used to represent the **Post** body when adding a blind.
 *
 * _This is a generated class from a json response._
 */
data class BlindPost(

    @field:SerializedName("Illnesses")
    val illnesses: List<String>,

    @field:SerializedName("SerialNumber")
    val serialNumber: String,

    @field:SerializedName("FirstName")
    val firstName: String,

    @field:SerializedName("PhoneNumber")
    val phoneNumber: String,

    @field:SerializedName("EmergencyPhoneNumber")
    val emergencyPhoneNumber: String,

    @field:SerializedName("LastName")
    val lastName: String, // TODO change based on the ui

    @field:SerializedName("Gender")
    val gender: Int,

    @field:SerializedName("File")
    val profilePicture: MultipartBody.Part,

    @field:SerializedName("Age")
    val age: Int,

    @field:SerializedName("BloodType")
    val bloodType: String
)
