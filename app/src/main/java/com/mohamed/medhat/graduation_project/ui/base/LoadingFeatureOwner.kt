package com.mohamed.medhat.graduation_project.ui.base

import androidx.lifecycle.ViewModel

/**
 * Implement this interface if the activity has a [ViewModel].
 */
interface LoadingFeatureOwner {
    /**
     * Shows the loading indicator in the activity.
     */
    fun showLoadingIndicator()

    /**
     * Hides the loading indicator in the activity.
     */
    fun hideLoadingIndicator()

    /**
     * Shows the error message in the activity.
     */
    fun showErrorMessage()

    /**
     * Hides the error message in the activity.
     */
    fun hideErrorMessage()

    /**
     * Sets the error message in the activity.
     * @param message the message to set.
     */
    fun setErrorMessage(message: String)
}