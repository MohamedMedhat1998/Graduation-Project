package com.mohamed.medhat.sanad.model

import com.google.gson.annotations.SerializedName

/**
 * A class representing the **GET** body of a single item of the endpoint that returns the favorite places of a blind
 *
 *
 * _This is a generated class from a json response._
 */
data class FavoritePlace(

    @field:SerializedName("phoneNumber")
    val phoneNumber: String,

    @field:SerializedName("dateCreated")
    val dateCreated: String,

    @field:SerializedName("latitude")
    val latitude: Float,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("longitude")
    val longitude: Float
)
