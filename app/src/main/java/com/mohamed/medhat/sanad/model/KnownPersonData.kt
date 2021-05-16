package com.mohamed.medhat.sanad.model

import com.google.gson.annotations.SerializedName

/**
 * A class representing the **POST** body of the endpoint that registers new known persons for a blind.
 *
 *
 * _This is a generated class from a json response._
 */
data class KnownPersonData(

    @field:SerializedName("reminderAbout")
    val reminderAbout: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("mentorUsername")
    val mentorUsername: String,

    @field:SerializedName("blindUsername")
    val blindUsername: String
)
