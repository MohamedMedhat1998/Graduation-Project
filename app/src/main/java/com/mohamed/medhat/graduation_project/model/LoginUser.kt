package com.mohamed.medhat.graduation_project.model

import com.google.gson.annotations.SerializedName

/**
 * A data class used to represent the **Post** body when logging into an existing user.
 *
 * _This is a generated class from a json response._
 */
data class LoginUser(

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("email")
	val email: String
)
