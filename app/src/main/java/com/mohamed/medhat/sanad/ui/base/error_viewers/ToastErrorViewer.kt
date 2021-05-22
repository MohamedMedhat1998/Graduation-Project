package com.mohamed.medhat.sanad.ui.base.error_viewers

import com.mohamed.medhat.sanad.model.error.AppError
import com.mohamed.medhat.sanad.model.error.SimpleConnectionError
import com.mohamed.medhat.sanad.model.error.SingleLineError
import com.mohamed.medhat.sanad.ui.base.BaseActivity
import com.mohamed.medhat.sanad.utils.exceptions.IllegalAppErrorTypeException

/**
 * A class that displays an error inside a toast.
 */
class ToastErrorViewer(override val appError: AppError, val activity: BaseActivity) :
    AppErrorViewer {

    override fun display() {
        when (appError) {
            is SingleLineError -> {
                activity.displayToast(appError.errorLine)
            }
            is SimpleConnectionError -> {
                activity.displayToast(appError.description)
            }
            else -> {
                throw IllegalAppErrorTypeException(
                    SingleLineError::class.java.name,
                    appError.javaClass.name
                )
            }
        }
    }

    override fun hide() {
        // No implementation :/
    }
}