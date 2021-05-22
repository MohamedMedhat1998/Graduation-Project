package com.mohamed.medhat.sanad.model

import com.google.gson.annotations.SerializedName

/**
 * A data class used to represent the response body from the **GET** profile endpoint.
 *
 * _This is a generated class from a json response._
 */
data class MentorProfile(

	@field:SerializedName("firstName")
	val firstName: String,

	@field:SerializedName("lastName")
	val lastName: String,

	@field:SerializedName("profilePicture")
	val profilePicture: String?,

	@field:SerializedName("twoFactorEnabled")
	val twoFactorEnabled: Boolean,

	@field:SerializedName("dateCreated")
	val dateCreated: String,

	@field:SerializedName("phoneNumber")
	val phoneNumber: String,

	@field:SerializedName("gender")
	val gender: Int,

	@field:SerializedName("emailConfirmed")
	val emailConfirmed: Boolean,

	@field:SerializedName("dateModified")
	val dateModified: String,

	@field:SerializedName("age")
	val age: Int,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("phoneNumberConfirmed")
	val phoneNumberConfirmed: Boolean
)
