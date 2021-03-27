package com.mohamed.medhat.sanad.ui.base.error_viewers

import com.mohamed.medhat.sanad.model.error.AppError

/**
 * The error viewer family parent.
 */
interface AppErrorViewer {
    /**
     * The [AppError] to display to the user.
     */
    val appError: AppError

    /**
     * The display function is responsible for showing the error to the user.
     */
    fun display()

    /**
     * The hide function is responsible for hiding the error.
     */
    fun hide()
}