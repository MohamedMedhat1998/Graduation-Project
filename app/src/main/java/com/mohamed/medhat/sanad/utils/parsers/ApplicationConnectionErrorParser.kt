package com.mohamed.medhat.sanad.utils.parsers

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mohamed.medhat.sanad.model.error.DetailedConnectionError
import org.json.JSONException
import org.json.JSONObject


/**
 * A class used to parse JSON objects to an [DetailedConnectionError] object.
 */
class ApplicationConnectionErrorParser {

    companion object {

        /**
         * Parses the given string json object to an [DetailedConnectionError] object.
         * @param body the json-string.
         * @return the [DetailedConnectionError] object.
         */
        @JvmStatic
        fun parse(body: String): DetailedConnectionError {
            val root = JSONObject(body)
            // TODO undo this after the back end developer fixes it
            try {
                val id = root.getString("id")
                val statusCode = root.getInt("statusCode")
                val title = root.getString("title")
                val description = root.getString("description")
                val errorDate = root.getString("errorDate")
                val errorsMapJsonObject = root.getJSONObject("errors")
                val mapType = object : TypeToken<Map<String, List<String>>>() {}.type
                val errorsMap: Map<String, List<String>> =
                    Gson().fromJson(errorsMapJsonObject.toString(), mapType)
                return DetailedConnectionError(
                    id,
                    statusCode,
                    title,
                    description,
                    errorDate,
                    errorsMap
                )
            } catch (jsonE: JSONException) {
                jsonE.printStackTrace()
                val id = root.getString("Id")
                val statusCode = root.getInt("StatusCode")
                val title = root.getString("Title")
                val description = root.getString("Description")
                val errorDate = root.getString("ErrorDate")
                val errorsMapJsonObject = root.getJSONObject("Errors")
                val mapType = object : TypeToken<Map<String, List<String>>>() {}.type
                val errorsMap: Map<String, List<String>> =
                    Gson().fromJson(errorsMapJsonObject.toString(), mapType)
                return DetailedConnectionError(
                    id,
                    statusCode,
                    title,
                    description,
                    errorDate,
                    errorsMap
                )
            }
        }
    }
}
