package com.mohamed.medhat.sanad.model

import okhttp3.MultipartBody

/**
 * A data class used to represent the **Post** body when adding a blind.
 *
 * _This is a generated class from a json response._
 */
data class BlindPost(

    val illnesses: List<String>,

    val serialNumber: MultipartBody.Part,

    val firstName: MultipartBody.Part,

    val phoneNumber: MultipartBody.Part,

    val emergencyPhoneNumber: MultipartBody.Part,

    val lastName: MultipartBody.Part, // TODO change based on the ui

    val gender: MultipartBody.Part,

    val profilePicture: MultipartBody.Part,

    val age: MultipartBody.Part,

    val bloodType: MultipartBody.Part
)
