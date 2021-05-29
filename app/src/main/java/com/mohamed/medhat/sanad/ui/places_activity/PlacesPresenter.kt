package com.mohamed.medhat.sanad.ui.places_activity

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.model.BlindMiniProfile
import com.mohamed.medhat.sanad.model.FavoritePlace
import com.mohamed.medhat.sanad.model.GpsNode
import com.mohamed.medhat.sanad.networking.NetworkState
import com.mohamed.medhat.sanad.ui.base.AdvancedPresenter
import com.mohamed.medhat.sanad.ui.base.error_viewers.TextErrorViewer
import com.mohamed.medhat.sanad.ui.login_activity.LoginActivity
import com.mohamed.medhat.sanad.utils.FRAGMENT_FEATURES_BLIND_PROFILE
import com.mohamed.medhat.sanad.utils.MAP_CAMERA_ZOOM_LEVEL
import com.mohamed.medhat.sanad.utils.TAG_MARKER_ICON
import com.mohamed.medhat.sanad.utils.handleLoadingState
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_places.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * An mvp presenter for the [PlacesActivity].
 */
class PlacesPresenter @Inject constructor() : AdvancedPresenter<PlacesView, PlacesViewModel>() {

    private lateinit var placesView: PlacesView
    private lateinit var placesViewModel: PlacesViewModel
    private lateinit var activity: PlacesActivity
    private lateinit var map: GoogleMap
    private lateinit var marker: Marker
    private lateinit var blindMiniProfile: BlindMiniProfile
    private val markerOptions = MarkerOptions()
    private lateinit var location: GpsNode
    private var initialFavoriteLoad = true
    private val favoritePlacesMarkers = mutableMapOf<FavoritePlace, Marker>()

    override fun start(savedInstanceState: Bundle?) {
        loadMap()
        placesView.setBlindProfile(blindMiniProfile)
        runLocationUpdateThread()
        loadFavoritePlaces()
        placesViewModel.blindLocation.observe(activity) {
            location = it
            drawMarker(it)
        }
        placesViewModel.shouldReLogin.observe(activity) {
            placesView.startActivityAsRoot(LoginActivity::class.java)
        }
        placesViewModel.favoritePlaces.observe(activity) {
            if (it.isEmpty()) {
                placesView.displayToast("No favorite places found!")
            }
            drawFavoriteMarkers(it)
        }
        placesViewModel.state.observe(activity) {
            val textErrorViewer =
                TextErrorViewer(placesViewModel.appError, activity.tv_places_error)
            placesView.setAppErrorViewer(textErrorViewer)
            handleLoadingState(activity, it)
        }
    }

    /**
     * Reloads the favorite places of the blind person.
     */
    private fun loadFavoritePlaces() {
        if (initialFavoriteLoad) {
            placesViewModel.loadFavoritePlaces(blindMiniProfile)
            initialFavoriteLoad = false
        }
        NetworkState.isConnected.observe(activity) {
            if (it && !initialFavoriteLoad) {
                placesViewModel.loadFavoritePlaces(blindMiniProfile)
            }
        }
    }

    /**
     * Periodically updates the location of the blind.
     */
    private fun runLocationUpdateThread() {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                delay(2000)
                placesViewModel.updateLocation(blindMiniProfile)
            }
        }
    }

    override fun setView(view: PlacesView) {
        placesView = view
        activity = view as PlacesActivity
        blindMiniProfile =
            activity.intent.extras!!.getSerializable(FRAGMENT_FEATURES_BLIND_PROFILE) as BlindMiniProfile
    }

    override fun setViewModel(viewModel: PlacesViewModel) {
        placesViewModel = viewModel
    }

    fun handleOnMapReady(map: GoogleMap) {
        this.map = map
    }

    /**
     * Loads the google maps fragment instance.
     */
    private fun loadMap() {
        val mapFragment = SupportMapFragment.newInstance()
        activity.supportFragmentManager.beginTransaction()
            .add(R.id.fr_places_maps_container, mapFragment).commit()
        mapFragment.getMapAsync(placesView)
    }

    /**
     * Draws a new marker on the map or updates its location if it is there.
     * @param gpsNode The object that contains the coordinates of the marker.
     */
    private fun drawMarker(gpsNode: GpsNode) {
        if (::marker.isInitialized) {
            // Update the marker location.
            marker.position =
                LatLng(gpsNode.latitude.toDouble(), gpsNode.longitude.toDouble())
        } else {
            // Draw a new marker.
            marker = map.addMarker(
                markerOptions.position(
                    LatLng(
                        gpsNode.latitude.toDouble(),
                        gpsNode.longitude.toDouble()
                    )
                ).title("${blindMiniProfile.firstName} ${blindMiniProfile.lastName}").snippet("")
            )
            marker.updateImageFromUrl(blindMiniProfile.profilePicture)
            map.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(gpsNode.latitude.toDouble(), gpsNode.longitude.toDouble()),
                    MAP_CAMERA_ZOOM_LEVEL
                )
            )
        }
    }

    /**
     * An extension function to download an image from a url and display it for the marker.
     * @param url The link of the image.
     */
    fun Marker.updateImageFromUrl(url: String) {
        Glide.with(activity)
            .asBitmap()
            .circleCrop()
            .load(url)
            .into(object : CustomTarget<Bitmap>(60, 60) {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    this@updateImageFromUrl.setIcon(BitmapDescriptorFactory.fromBitmap(resource))
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    Log.e(
                        TAG_MARKER_ICON,
                        "Failed to retrieve the user icon... using the default marker instead."
                    )
                }
            })
    }

    fun handleOnResume() {
        if (NetworkState.isConnected.value == true) {
            placesViewModel.hasInternetConnection = true
        }
    }

    fun handleOnPause() {
        placesViewModel.hasInternetConnection = false
    }

    fun onProfilePictureClicked() {
        if (::marker.isInitialized && ::location.isInitialized) {
            map.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(location.latitude.toDouble(), location.longitude.toDouble()),
                    MAP_CAMERA_ZOOM_LEVEL
                )
            )
        }
    }

    /**
     * Draws favorite places pins on the map.
     * @param favoritePlaces The list of favorite places to draw their pins.
     */
    private fun drawFavoriteMarkers(favoritePlaces: List<FavoritePlace>) {
        val favoriteIcon =
            ContextCompat.getDrawable(activity, R.drawable.ic_favorite)!!.toBitmap(60, 60)
        if (::map.isInitialized) {
            favoritePlaces.forEach {
                val marker = map.addMarker(
                    markerOptions.position(
                        LatLng(it.latitude.toDouble(), it.longitude.toDouble())
                    ).title(it.name).snippet(it.phoneNumber)
                        .icon(BitmapDescriptorFactory.fromBitmap(favoriteIcon))
                )
                favoritePlacesMarkers[it] = marker
            }
        }
    }
}