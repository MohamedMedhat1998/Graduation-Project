package com.mohamed.medhat.sanad.utils.managers

import android.util.Log
import com.mohamed.medhat.sanad.local.SharedPrefs
import com.mohamed.medhat.sanad.model.Token
import com.mohamed.medhat.sanad.utils.parsers.StringToDateParser
import javax.inject.Inject

private const val TOKEN_MANAGER_TAG = "TOKEN_MANAGER"

const val TOKEN = "token"
private const val TOKEN_EXPIRATION_DATE = "token-expiration-date"
private const val REFRESH_TOKEN = "refresh-token"
private const val REFRESH_TOKEN_EXPIRATION_DATE = "refresh-token-expiration-date"

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
        sharedPrefs.write(TOKEN_EXPIRATION_DATE, token.expiration)
        sharedPrefs.write(REFRESH_TOKEN, token.refreshToken)
        sharedPrefs.write(REFRESH_TOKEN_EXPIRATION_DATE, token.refreshTokenExpiration)
        Log.d(TOKEN_MANAGER_TAG, "Token Saved!")
    }

    /**
     * Retrieves the stored token from the shared preferences.
     * @return the token string.
     */
    fun getToken(): String {
        Log.d(TOKEN_MANAGER_TAG, "token read")
        return sharedPrefs.read(TOKEN)
    }

    /**
     * Retrieves the stored refresh token from the shared preferences.
     * @return the refresh token string.
     */
    fun getRefreshToken(): String {
        Log.d(TOKEN_MANAGER_TAG, "refresh-token read")
        return sharedPrefs.read(REFRESH_TOKEN)
    }

    /**
     * Checks whether the token is expired or not.
     * @param currentDateString the current date of the day.
     * This parameter must be provided to the function for simplicity.
     * @return `true` if the token expiration date is before `currentDate` parameter, `false` otherwise.
     */
    fun isTokenExpired(currentDateString: String): Boolean {
        val expirationDate = StringToDateParser.parse(sharedPrefs.read(TOKEN_EXPIRATION_DATE))
        val currentDate = StringToDateParser.parse(currentDateString)
        val result = expirationDate?.before(currentDate) ?: true
        if (result) {
            Log.d(TOKEN_MANAGER_TAG, "token expired")
            Log.d(
                TOKEN_MANAGER_TAG,
                "expiration date: ${expirationDate.toString()}\ncurrent date: ${currentDate.toString()}"
            )
        } else {
            Log.d(TOKEN_MANAGER_TAG, "token is valid")
            Log.d(
                TOKEN_MANAGER_TAG,
                "expiration date: ${expirationDate.toString()}\ncurrent date: ${currentDate.toString()}"
            )
        }
        return result
    }

    /**
     * Removes all the saved tokens.
     */
    fun clearToken() {
        sharedPrefs.clear(
            TOKEN,
            TOKEN_EXPIRATION_DATE,
            REFRESH_TOKEN,
            REFRESH_TOKEN_EXPIRATION_DATE
        )
        Log.d(TOKEN_MANAGER_TAG, "Token cleared!")
    }
}