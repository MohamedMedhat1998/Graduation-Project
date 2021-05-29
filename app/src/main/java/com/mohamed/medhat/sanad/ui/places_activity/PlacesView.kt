package com.mohamed.medhat.sanad.ui.places_activity

import com.google.android.gms.maps.OnMapReadyCallback
import com.mohamed.medhat.sanad.model.BlindMiniProfile
import com.mohamed.medhat.sanad.ui.base.BaseView
import com.mohamed.medhat.sanad.ui.base.LoadingFeatureOwner

/**
 * An mvp view for the [PlacesActivity].
 */
interface PlacesView : BaseView, OnMapReadyCallback, LoadingFeatureOwner {
    fun setBlindProfile(blindMiniProfile: BlindMiniProfile)
}