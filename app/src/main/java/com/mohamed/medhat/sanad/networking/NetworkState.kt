package com.mohamed.medhat.sanad.networking

import androidx.lifecycle.MutableLiveData

/**
 * Used to check the internet connection. This is a singleton object that is accessible everywhere.
 */
object NetworkState {
    /**
     * `True` if there is a network connection, `false` otherwise
     */
    val isConnected = MutableLiveData<Boolean>()
}