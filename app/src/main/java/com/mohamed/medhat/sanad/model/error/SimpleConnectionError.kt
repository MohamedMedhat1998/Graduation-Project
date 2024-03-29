package com.mohamed.medhat.sanad.model.error

/**
 * A simple connection error class that only contains the title and the description of the error.
 */
data class SimpleConnectionError(val title: String, val description: String) : AppError
