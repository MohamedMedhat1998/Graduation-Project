package com.mohamed.medhat.sanad.local

import android.content.SharedPreferences
import android.util.Log
import com.mohamed.medhat.sanad.utils.BLANK
import com.mohamed.medhat.sanad.utils.PREFS_CLEAR
import com.mohamed.medhat.sanad.utils.PREFS_READ
import com.mohamed.medhat.sanad.utils.PREFS_WRITE
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A class responsible for accessing the shared preferences of the app.
 */
@Singleton
class SharedPrefs @Inject constructor(val sharedPreferences: SharedPreferences) {

    /**
     * Reads a string value from the shared preferences of the app.
     * @param key the key of the target value.
     * @return the target value if it exists, an empty string otherwise.
     */
    fun read(key: String): String {
        val result = sharedPreferences.getString(key, BLANK) ?: BLANK
        Log.d(PREFS_READ, "\"$result\" was read from \"$key\"!")
        return result
    }

    /**
     * Writes a value to the shared preferences of the app.
     * @param key the key associated with this value.
     * @param value the actual value to store.
     */
    fun write(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
        Log.d(PREFS_WRITE, "\"$value\" was stored at \"$key\"!")
    }

    /**
     * Removes all the entities from the shared preferences.
     */
    fun clearAll() {
        val editor = sharedPreferences.edit()
        sharedPreferences.all.keys.forEach {
            editor.remove(it)
        }
        editor.apply()
        Log.d(PREFS_CLEAR, "Cache was cleared!")
    }

    /**
     * Removes all the passed keys from the shared preferences.
     * @param keys the keys to clear.
     */
    fun clear(vararg keys: String) {
        val editor = sharedPreferences.edit()
        keys.forEach {
            editor.remove(it)
            Log.d(PREFS_CLEAR, "\"$it\" was cleared")
        }
        editor.apply()
    }
}