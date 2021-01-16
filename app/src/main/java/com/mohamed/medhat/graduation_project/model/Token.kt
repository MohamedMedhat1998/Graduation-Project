package com.mohamed.medhat.graduation_project.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * A data class used to represent the **Post** response when creating a new user or login into an existing user.
 *
 * _This is a generated class from a json response._
 */
@Parcelize
data class Token(
	val expiration: String,
	val token: String
) : Parcelable {

    override fun toString(): String {
        return "Token[${hashCode()}](token: ${token}, expiration: $expiration)"
    }
}
