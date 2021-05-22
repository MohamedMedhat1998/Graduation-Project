package com.mohamed.medhat.sanad.ui.mentor_picture_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mohamed.medhat.sanad.networking.WebApi
import com.mohamed.medhat.sanad.ui.base.BaseViewModel
import javax.inject.Inject

/**
 * A ViewModel that figures out the next destination screen for the [MentorPictureActivity].
 */
class MentorPictureNavViewModel @Inject constructor(val webApi: WebApi) : BaseViewModel() {
    private val _destination = MutableLiveData<Class<*>>()
    val destination: LiveData<Class<*>>
        get() = _destination

    fun calculateDestination() {

    }
}