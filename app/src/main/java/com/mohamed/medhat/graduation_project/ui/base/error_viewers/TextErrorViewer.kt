package com.mohamed.medhat.graduation_project.ui.base.error_viewers

import android.view.View
import android.widget.TextView
import com.mohamed.medhat.graduation_project.model.error.AppError
import com.mohamed.medhat.graduation_project.model.error.DetailedConnectionError
import com.mohamed.medhat.graduation_project.model.error.NoError
import com.mohamed.medhat.graduation_project.model.error.SimpleConnectionError
import com.mohamed.medhat.graduation_project.utils.exceptions.IllegalAppErrorTypeException

/**
 * Responsible for displaying text type errors only.
 * @param appError the error object.
 * @param textView the [TextView] to display the error object inside.
 */
class TextErrorViewer(override val appError: AppError, private val textView: TextView) :
    AppErrorViewer {
    override fun display() {
        when (appError) {
            is DetailedConnectionError -> {
                val builder =
                    StringBuilder().append(appError.title).append(": ")
                        .appendLine(appError.description)
                appError.errors.entries.forEach { map ->
                    builder.append("  • ").append(map.key).appendLine(":")
                    map.value.forEach { errorLine ->
                        builder.append("    ‣").appendLine(errorLine)
                    }
                }
                textView.text = builder.toString()
                textView.visibility = View.VISIBLE
            }
            is SimpleConnectionError -> {
                val errorString =
                    StringBuilder().appendLine(appError.title).append(appError.description)
                        .toString()
                textView.text = errorString
                textView.visibility = View.VISIBLE
            }
            is NoError -> {
                hide()
                textView.text = ""
            }
            else -> {
                hide()
                throw (IllegalAppErrorTypeException(
                    "TextError",
                    appError.javaClass.name
                ))
            }
        }
    }

    override fun hide() {
        textView.visibility = View.INVISIBLE
    }
}