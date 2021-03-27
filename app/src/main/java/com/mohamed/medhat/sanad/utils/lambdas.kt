package com.mohamed.medhat.sanad.utils

import com.mohamed.medhat.sanad.model.error.AppError
import com.mohamed.medhat.sanad.model.error.SimpleConnectionError
import com.mohamed.medhat.sanad.networking.NetworkState
import com.mohamed.medhat.sanad.ui.base.LoadingFeatureOwner
import com.mohamed.medhat.sanad.ui.helpers.State
import com.mohamed.medhat.sanad.utils.parsers.ApplicationConnectionErrorParser
import retrofit2.Response

/**
 * Controls the visibility of the error message and loading indicator.
 */
val handleLoadingState: (loadingFeatureOwner: LoadingFeatureOwner, state: State) -> Unit =
    { loadingFeatureOwner: LoadingFeatureOwner, state: State ->
        // TODO handle authorization exception
        when (state) {
            State.NORMAL -> {
                loadingFeatureOwner.apply {
                    hideError()
                    hideLoadingIndicator()
                }
            }
            State.ERROR -> {
                loadingFeatureOwner.apply {
                    showError()
                    hideLoadingIndicator()
                }
            }
            State.LOADING -> {
                loadingFeatureOwner.apply {
                    hideError()
                    showLoadingIndicator()
                }
            }
        }
    }

/**
 * Creates a [SimpleConnectionError] object from a given [Throwable] object.
 */
val catchUnknownError: (t: Throwable) -> AppError = { t ->
    t.printStackTrace()
    val appError = if (NetworkState.isConnected.value == false) {
        SimpleConnectionError(
            "Network Error!",
            "Please check your internet connection!"
        )
    } else {
        SimpleConnectionError("Something went wrong!", t.message.toString())
    }
    appError
}

/**
 * Creates a suitable server or response error from a given response.
 * @param response the response from the request to build the error based upon.
 */
fun <T> catchServerOrResponseError(response: Response<T>): AppError {
    return if (response.code() in 400..499) {
        val errorBody = response.errorBody()?.string()
        ApplicationConnectionErrorParser.parse(errorBody.toString())
    } else {
        SimpleConnectionError(
            "Something went wrong!",
            response.message()
        )
    }
}