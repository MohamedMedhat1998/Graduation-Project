package com.mohamed.medhat.graduation_project.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * A data class used to represent the **Post** body when creating a new user.
 *
 * _This is a generated class from a json response._
 */
@Parcelize
data class NewUser(
	val password: String,
	val fullName: String,
	val confirmPassword: String,
	val email: String
) : Parcelable
