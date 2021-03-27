package com.mohamed.medhat.sanad.utils.exceptions

import com.mohamed.medhat.sanad.model.error.AppError
import com.mohamed.medhat.sanad.ui.base.error_viewers.AppErrorViewer

/**
 * An exception class that is used to indicate that the [AppError] isn't suitable for the [AppErrorViewer].
 * @param expected the expected [AppError] type class name.
 * @param found the provided [AppError] type class name.
 */
class IllegalAppErrorTypeException(expected: String, found: String) :
    Exception("Illegal Error Type: Expected an AppError of type \"$expected\" but found \"$found\"")