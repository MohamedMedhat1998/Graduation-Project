package com.mohamed.medhat.sanad.model

import com.google.gson.annotations.SerializedName

/**
 * A data class used to represent the **Post** body when adding a favorite place.
 *
 * _This is a generated class from a json response._
 */
data class FavoritePlacePost(

    @field:SerializedName("phoneNumber")
    val phoneNumber: String,

    @field:SerializedName("latitude")
    val latitude: Int,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("longitude")
    val longitude: Int
)
