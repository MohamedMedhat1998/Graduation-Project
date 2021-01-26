package com.mohamed.medhat.graduation_project.networking

/**
 * Used to check the internet connection. This is a singleton object that is accessible everywhere.
 */
object NetworkState {
    /**
     * `True` if there is a network connection, `false` otherwise
     */
    var isConnected: Boolean = false
}