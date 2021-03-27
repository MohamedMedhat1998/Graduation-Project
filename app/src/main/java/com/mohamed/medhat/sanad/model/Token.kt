package com.mohamed.medhat.sanad.model

/**
 * A data class used to represent the **Post** response when creating a new user or login into an existing user.
 *
 * _This is a generated class from a json response._
 */
data class Token(
    val refreshTokenExpiration: String,
    val expiration: String,
    val token: String,
    val refreshToken: String
) {

    override fun toString(): String {
        return "Token[${hashCode()}](token: $token, expiration: $expiration, refreshToken: $refreshToken, refreshTokenExpiration: $refreshTokenExpiration)"
    }
}
