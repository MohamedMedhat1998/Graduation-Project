package com.mohamed.medhat.sanad.model

import com.google.gson.annotations.SerializedName
import com.mohamed.medhat.sanad.ui.main_activity.blinds.BlindItem
import java.io.Serializable

/**
 * A data class used to represent the a single item in the response of the `blinds` **GET** request.
 *
 * _This is a generated class from a json response._
 */
data class BlindMiniProfile(

    @field:SerializedName("firstName")
    val firstName: String,

    @field:SerializedName("lastName")
    val lastName: String,

    @field:SerializedName("profilePicture")
    val profilePicture: String,

    @field:SerializedName("userName")
    val userName: String
) : BlindItem, Serializable
