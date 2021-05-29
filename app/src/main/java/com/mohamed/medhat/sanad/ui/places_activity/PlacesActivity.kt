package com.mohamed.medhat.sanad.ui.places_activity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.model.BlindMiniProfile
import com.mohamed.medhat.sanad.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_places.*
import javax.inject.Inject

/**
 * An activity that enables the mentor to explore the favorite places of a blind and to add a new
 * place to the list.
 */
class PlacesActivity : BaseActivity(), PlacesView {

    @Inject
    lateinit var placesPresenter: PlacesPresenter
    private lateinit var map: GoogleMap

    private val placesViewModel: PlacesViewModel by lazy {
        ViewModelProviders.of(this, activityComponent.placesViewModel())
            .get(PlacesViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_places)
        activityComponent.inject(this)
        placesPresenter.setView(this)
        placesPresenter.setViewModel(placesViewModel)
        placesPresenter.start(savedInstanceState)
        iv_places_profile_pic.setOnClickListener {
            placesPresenter.onProfilePictureClicked()
        }
    }

    override fun onPause() {
        super.onPause()
        placesPresenter.handleOnPause()
    }

    override fun onResume() {
        super.onResume()
        placesPresenter.handleOnResume()
    }

    override fun setBlindProfile(blindMiniProfile: BlindMiniProfile) {
        tv_places_name.text = getString(
            R.string.blind_profile_name_holder,
            blindMiniProfile.firstName,
            blindMiniProfile.lastName
        )
        // TODO Battery percentage
        // TODO isOnline
        Glide.with(this)
            .load(blindMiniProfile.profilePicture)
            .circleCrop()
            .into(iv_places_profile_pic)
    }

    override fun onMapReady(p0: GoogleMap?) {
        if (p0 != null) {
            map = p0
            placesPresenter.handleOnMapReady(map)
        }
    }

    override fun showLoadingIndicator() {
        pb_places_loading.visibility = View.VISIBLE
    }

    override fun hideLoadingIndicator() {
        pb_places_loading.visibility = View.INVISIBLE
    }

    override fun showError() {
        displayAppError()
    }

    override fun hideError() {
        hideAppError()
    }
}