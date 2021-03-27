package com.mohamed.medhat.sanad.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mohamed.medhat.sanad.model.error.AppError
import com.mohamed.medhat.sanad.model.error.NoError
import com.mohamed.medhat.sanad.ui.helpers.State

/**
 * A base class for all the [ViewModel] classes inside the project.
 * This class will contain whatever is common between all the [ViewModel] classes inside the project.
 */
open class BaseViewModel : ViewModel() {
    /**
     * Indicates the current [State] of the [ViewModel].
     */
    protected val _state = MutableLiveData<State>()

    /**
     * Indicates the current [State] of the [ViewModel].
     */
    val state: LiveData<State>
        get() = _state

    /**
     * An error object that should be built if the [State] is [State.ERROR].
     */
    var appError: AppError = NoError()
}