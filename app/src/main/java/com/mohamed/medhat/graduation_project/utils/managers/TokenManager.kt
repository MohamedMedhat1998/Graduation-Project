package com.mohamed.medhat.graduation_project.utils.managers

import com.mohamed.medhat.graduation_project.local.SharedPrefs
import com.mohamed.medhat.graduation_project.model.Token
import javax.inject.Inject

private const val TOKEN = "token"
private const val REFRESH_TOKEN = "refresh-token"

/**
 * A class used for saving and retrieving a token.
 */
class TokenManager @Inject constructor(val sharedPrefs: SharedPrefs) {
    /**
     * Stores a token to the shared preferences.
     * @param token the token to save.
     */
    fun save(token: Token) {
        sharedPrefs.write(TOKEN, token.token)
        sharedPrefs.write(REFRESH_TOKEN, token.refreshToken)
    }

    /**
     * Retrieves the stored token from the shared preferences.
     * @return the token string.
     */
    fun getToken(): String {
        return sharedPrefs.read(TOKEN)
    }

    /**
     * Retrieves the stored refresh token from the shared preferences.
     * @return the refresh token string.
     */
    fun getRefreshToken(): String {
        return sharedPrefs.read(REFRESH_TOKEN)
    }
}