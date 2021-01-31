package com.mohamed.medhat.graduation_project.model

/**
 * A data class used to represent the **Post** body when registering a new user.
 *
 * _This is a generated class from a json response._
 */
data class NewUser(
    val firstName: String,
    val lastName: String,
    val password: String,
    val confirmPassword: String,
    val email: String
)

