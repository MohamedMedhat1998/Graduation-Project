package com.mohamed.medhat.sanad.ui.helpers

import androidx.lifecycle.ViewModel
import com.mohamed.medhat.sanad.ui.helpers.State.*
import java.io.Serializable

/**
 * Used to indicate the state of the current [ViewModel].
 * It can be one of the following:
 * 1. [NORMAL]: indicates that nothing is loading and no errors exist.
 * 2. [ERROR]: indicates that something went wrong.
 * 3. [LOADING]: indicates that something is loading.
 */
sealed class State : Serializable {
    /**
     * A [State] that indicates that nothing is loading and no errors exist.
     */
    object NORMAL : State()

    /**
     * A [State] that indicates that something went wrong.
     */
    object ERROR : State()

    /**
     * A [State] that indicates that something is loading.
     */
    object LOADING : State()
}
