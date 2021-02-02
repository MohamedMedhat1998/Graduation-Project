package com.mohamed.medhat.graduation_project.ui.base.error_viewers

import android.util.Log
import com.mohamed.medhat.graduation_project.model.error.AppError
import com.mohamed.medhat.graduation_project.model.error.NoError
import com.mohamed.medhat.graduation_project.utils.APP_ERROR_FAMILY

/**
 * A non-error viewer that displays nothing.
 */
class NoErrorViewer : AppErrorViewer {

    override val appError: AppError
        get() = NoError()

    override fun display() {
        Log.d(APP_ERROR_FAMILY, "$appError is displayed")
    }

    override fun hide() {
        Log.d(APP_ERROR_FAMILY, "$appError is hidden")
    }
}