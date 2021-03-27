package com.mohamed.medhat.sanad.model.error

/**
 * A class represents no-error.
 */
class NoError : AppError {
    override fun toString(): String {
        return "${this::class.java.simpleName}: No Errors"
    }
}