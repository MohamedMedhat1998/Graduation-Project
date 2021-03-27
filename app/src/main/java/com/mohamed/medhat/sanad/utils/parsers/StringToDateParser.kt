package com.mohamed.medhat.sanad.utils.parsers

import java.text.SimpleDateFormat
import java.util.*

/**
 * A helper class used to convert a string object to a date object.
 */
class StringToDateParser {
    companion object {
        /**
         * A helper function that parses a string object to a [Date] object.
         */
        @JvmStatic
        fun parse(dateString: String): Date? {
            val newDate = dateString.replace('T', ' ')
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
            return dateFormat.parse(newDate)
        }
    }
}