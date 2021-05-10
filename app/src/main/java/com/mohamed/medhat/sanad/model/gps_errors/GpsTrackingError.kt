package com.mohamed.medhat.sanad.model.gps_errors

/**
 * A [GpsError] that occur when the blind's device has disabled mentor's tracking.
 */
class GpsTrackingError : GpsError {
    override val error: String
        get() = "Tracking is disabled!"
}