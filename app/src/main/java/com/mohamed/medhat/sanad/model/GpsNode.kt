package com.mohamed.medhat.sanad.model

import com.google.gson.annotations.SerializedName

/**
 * Represents the location of some node on the map.
 */
data class GpsNode(

    @field:SerializedName("latitude")
    val latitude: Float,

    @field:SerializedName("longitude")
    val longitude: Float
)
