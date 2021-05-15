package com.mohamed.medhat.sanad.ui.main_activity

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.dagger.scopes.ActivityScope
import com.mohamed.medhat.sanad.local.SharedPrefs
import com.mohamed.medhat.sanad.model.BlindAddProfile
import com.mohamed.medhat.sanad.model.BlindMiniProfile
import com.mohamed.medhat.sanad.model.GpsNode
import com.mohamed.medhat.sanad.model.gps_errors.GpsError
import com.mohamed.medhat.sanad.model.gps_errors.GpsNoLocationError
import com.mohamed.medhat.sanad.model.gps_errors.GpsTrackingError
import com.mohamed.medhat.sanad.networking.NetworkState
import com.mohamed.medhat.sanad.ui.base.AdvancedPresenter
import com.mohamed.medhat.sanad.ui.base.error_viewers.TextErrorViewer
import com.mohamed.medhat.sanad.ui.login_activity.LoginActivity
import com.mohamed.medhat.sanad.ui.main_activity.blinds.BlindItem
import com.mohamed.medhat.sanad.ui.main_activity.blinds.BlindsAdapter
import com.mohamed.medhat.sanad.ui.main_activity.features.FeaturesBottomFragment
import com.mohamed.medhat.sanad.ui.q_r_activity.scanner.ScannerActivity
import com.mohamed.medhat.sanad.utils.MAP_CAMERA_ZOOM_LEVEL
import com.mohamed.medhat.sanad.utils.TAG_FRAGMENT_FEATURES
import com.mohamed.medhat.sanad.utils.TAG_MARKER_ICON
import com.mohamed.medhat.sanad.utils.handleLoadingState
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * An mvp presenter for the main screen.
 */
@ActivityScope
class MainPresenter @Inject constructor(val sharedPrefs: SharedPrefs) :
    AdvancedPresenter<MainView, MainViewModel>() {

    private lateinit var mainView: MainView
    private lateinit var mainViewModel: MainViewModel
    private lateinit var activity: MainActivity
    private lateinit var blindsRecyclerView: RecyclerView
    private lateinit var blindsAdapter: BlindsAdapter
    private lateinit var map: GoogleMap
    private var shouldTerminate = false
    private var positions = mutableMapOf<String, Marker>()

    override fun start(savedInstanceState: Bundle?) {
        activity = mainView as MainActivity
        loadMap()
        initializeBlindsRecyclerView()
        NetworkState.isConnected.observe(activity) {
            shouldTerminate = !it
            if (it) {
                mainViewModel.reloadBlindProfiles()
            }
        }
        mainViewModel.blinds.observe(activity) {
            runPositionsThread(it)
            val listWithAddButton = mutableListOf<BlindItem>()
            listWithAddButton.addAll(it)
            listWithAddButton.add(BlindAddProfile())
            blindsAdapter.updateBlindsList(listWithAddButton)
        }
        mainViewModel.position.observe(activity) {
            drawMarkers(it)
        }
        mainViewModel.shouldReLogin.observe(activity) {
            if (it) {
                mainView.startActivityAsRoot(LoginActivity::class.java)
            }
        }
        mainViewModel.state.observe(activity) {
            val textErrorViewer = TextErrorViewer(mainViewModel.appError, activity.tv_main_error)
            mainView.setAppErrorViewer(textErrorViewer)
            handleLoadingState(activity, it)
        }
        mainViewModel.gpsErrors.observe(activity) {
            Log.d("GPS_ERRORS", "observed, ${it.entries.size} items")
            updateGpsErrors(it)
        }
    }

    override fun setView(view: MainView) {
        mainView = view
    }

    override fun setViewModel(viewModel: MainViewModel) {
        mainViewModel = viewModel
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
            .add(R.id.fr_main_maps_container, mapFragment).commit()
        mapFragment.getMapAsync(mainView)
    }

    /**
     * Initializes the recycler view of the blinds list.
     */
    private fun initializeBlindsRecyclerView() {
        blindsAdapter = BlindsAdapter(mutableListOf(), {
            mainView.navigateTo(ScannerActivity::class.java)
        }) {
            if (positions.containsKey(it.userName)) {
                map.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        positions[it.userName]?.position,
                        MAP_CAMERA_ZOOM_LEVEL
                    ), object : GoogleMap.CancelableCallback {
                        override fun onFinish() {
                            positions[it.userName]?.showInfoWindow()
                            FeaturesBottomFragment.newInstance(it).show(
                                activity.supportFragmentManager,
                                TAG_FRAGMENT_FEATURES
                            )
                        }
                        override fun onCancel() {}
                    }
                )
            } else {
                FeaturesBottomFragment.newInstance(it).show(
                    activity.supportFragmentManager,
                    TAG_FRAGMENT_FEATURES
                )
            }
        }
        blindsRecyclerView = activity.rv_main_blinds_list
        blindsRecyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        blindsRecyclerView.adapter = blindsAdapter
    }

    private fun runPositionsThread(blindsList: List<BlindMiniProfile>) {
        CoroutineScope(Dispatchers.IO).launch {
            while (!shouldTerminate) {
                delay(2000)
                mainViewModel.updatePositions(blindsList)
                Log.d("position", "updated!")
            }
        }
    }

    /**
     * Adds location markers on the map if they don't exist and updates the current markers if they exist.
     * @param blindsNodes A map that contains [BlindMiniProfile] as a key and its corresponding location [GpsNode] as a value.
     */
    private fun drawMarkers(blindsNodes: Map<BlindMiniProfile, GpsNode?>) {
        blindsNodes.entries.forEachIndexed { index, it ->
            val blindProfile = it.key
            val gpsNode = it.value
            if (!positions.containsKey(blindProfile.userName)) {
                Log.d("Marker", "New")
                // Draw a new marker if there were no markers before.
                if (::map.isInitialized) {
                    if (gpsNode != null) {
                        val marker = map.addMarker(
                            MarkerOptions().position(
                                LatLng(
                                    gpsNode.latitude.toDouble(),
                                    gpsNode.longitude.toDouble()
                                )
                            ).title("${blindProfile.firstName} ${blindProfile.lastName}")
                        )
                        marker.updateImageFromUrl(blindProfile.profilePicture)
                        positions[blindProfile.userName] = marker
                        if (index == 0) {
                            // Moving camera to the first item.
                            map.animateCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    marker.position,
                                    MAP_CAMERA_ZOOM_LEVEL
                                )
                            )
                        }
                    } // TODO handle null cases
                }
            } else {
                // Update the marker if it exists.
                if (gpsNode != null) {
                    Log.d("Marker", "Update")
                    val marker = positions[blindProfile.userName]
                    if (marker != null) {
                        Log.d("Marker", "Marker from the map is NOT null")
                        marker.position =
                            LatLng(gpsNode.latitude.toDouble(), gpsNode.longitude.toDouble())
                        marker.title = "${blindProfile.firstName} ${blindProfile.lastName}"
                        blindsAdapter.resetEntityError(blindProfile.userName)
                    } else {
                        Log.d("Marker", "Marker from the map is null")
                    }
                } // TODO handle null cases
            }
        }
    }

    /**
     * Displays a suitable error if there was a problem with the gps location.
     *@param gpsErrors A map that contains [BlindMiniProfile.userName] as a key and its corresponding error [GpsError].
     */
    private fun updateGpsErrors(gpsErrors: Map<String, GpsError>) {
        gpsErrors.entries.forEach {
            val username = it.key
            when (val gpsError = it.value) {
                is GpsTrackingError -> {
                    blindsAdapter.setEntityError(username, gpsError.error)
                }
                is GpsNoLocationError -> {
                    blindsAdapter.setEntityError(username, gpsError.error)
                }
            }
        }
    }

    fun handleOnCreateOptionsMenu(menu: Menu?) {
        activity.menuInflater.inflate(R.menu.menu_main, menu)
    }

    fun handleOnOptionItemSelected(item: MenuItem) {
        when (item.itemId) {
            R.id.mi_main_logout -> {
                sharedPrefs.clearAll()
                mainView.startActivityAsRoot(LoginActivity::class.java)
            }
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
}