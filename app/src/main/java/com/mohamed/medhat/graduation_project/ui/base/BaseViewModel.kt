package com.mohamed.medhat.graduation_project.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mohamed.medhat.graduation_project.ui.helpers.State

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
}