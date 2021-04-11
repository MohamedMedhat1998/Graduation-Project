package com.mohamed.medhat.sanad.ui.base.error_viewers

import android.util.Log
import com.mohamed.medhat.sanad.model.error.AppError
import com.mohamed.medhat.sanad.model.error.NoError
import com.mohamed.medhat.sanad.utils.TAG_APP_ERROR_FAMILY

/**
 * A non-error viewer that displays nothing.
 */
class NoErrorViewer : AppErrorViewer {

    override val appError: AppError
        get() = NoError()

    override fun display() {
        Log.d(TAG_APP_ERROR_FAMILY, "$appError is displayed")
    }

    override fun hide() {
        Log.d(TAG_APP_ERROR_FAMILY, "$appError is hidden")
    }
}