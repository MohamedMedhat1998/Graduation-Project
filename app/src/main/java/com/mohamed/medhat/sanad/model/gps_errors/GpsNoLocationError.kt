package com.mohamed.medhat.sanad.model.gps_errors

/**
 * A [GpsError] that occur when the blind's device didn't send any information about its location yet.
 *
 * Most probably, the device hasn't been activated yet.
 */
class GpsNoLocationError : GpsError {
    override val error: String
        get() = "No locations yet!"
}