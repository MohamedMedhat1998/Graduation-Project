package com.mohamed.medhat.graduation_project.ui.base

import androidx.lifecycle.ViewModel
import com.mohamed.medhat.graduation_project.model.error.DetailedConnectionError

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
     * Shows the error in the activity.
     */
    fun showError()

    /**
     * Hides the error in the activity.
     */
    fun hideError()
}