package com.mohamed.medhat.graduation_project.utils

import com.mohamed.medhat.graduation_project.ui.base.LoadingFeatureOwner
import com.mohamed.medhat.graduation_project.ui.helpers.State

/**
 * Controls the visibility of the error message and loading indicator.
 */
val handleLoadingState: (loadingFeatureOwner: LoadingFeatureOwner, error: String, state: State) -> Unit =
    { loadingFeatureOwner: LoadingFeatureOwner, error: String, state: State ->
        loadingFeatureOwner.setErrorMessage(error)
        when (state) {
            State.NORMAL -> {
                loadingFeatureOwner.apply {
                    hideErrorMessage()
                    hideLoadingIndicator()
                }
            }
            State.ERROR -> {
                loadingFeatureOwner.apply {
                    showErrorMessage()
                    hideLoadingIndicator()
                }
            }
            State.LOADING -> {
                loadingFeatureOwner.apply {
                    hideErrorMessage()
                    showLoadingIndicator()
                }
            }
        }
    }