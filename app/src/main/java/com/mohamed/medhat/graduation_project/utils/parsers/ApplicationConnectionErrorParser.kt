package com.mohamed.medhat.graduation_project.utils.parsers

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mohamed.medhat.graduation_project.model.error.DetailedConnectionError
import org.json.JSONObject


/**
 * A class used to parse JSON objects to an [DetailedConnectionError] object.
 */
object ApplicationConnectionErrorParser {

    /**
     * Parses the given string json object to an [DetailedConnectionError] object.
     * @param body the json-string.
     * @return the [DetailedConnectionError] object.
     */
    fun parse(body: String): DetailedConnectionError {
        val root = JSONObject(body)
        val id = root.getString("id")
        val statusCode = root.getInt("statusCode")
        val title = root.getString("title")
        val description = root.getString("description")
        val errorDate = root.getString("errorDate")
        val errorsMapJsonObject = root.getJSONObject("errors")
        val mapType = object : TypeToken<Map<String, List<String>>>() {}.type
        val errorsMap: Map<String, List<String>> =
            Gson().fromJson(errorsMapJsonObject.toString(), mapType)
        return DetailedConnectionError(id, statusCode, title, description, errorDate, errorsMap)
    }
}
