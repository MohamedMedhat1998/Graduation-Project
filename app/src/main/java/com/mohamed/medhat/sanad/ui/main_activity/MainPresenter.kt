package com.mohamed.medhat.sanad.ui.main_activity

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.dagger.scopes.ActivityScope
import com.mohamed.medhat.sanad.model.BlindMiniProfile
import com.mohamed.medhat.sanad.model.GpsNode
import com.mohamed.medhat.sanad.ui.base.AdvancedPresenter
import com.mohamed.medhat.sanad.ui.main_activity.blinds.BlindsAdapter
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
class MainPresenter @Inject constructor() :
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
        mainViewModel.blinds.observe(activity) {
            runPositionsThread(it)
            blindsAdapter.updateBlindsList(it)
        }
        mainViewModel.position.observe(activity) {
            drawMarkers(it)
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
        blindsAdapter = BlindsAdapter(mutableListOf())
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
     * @param blindsNodes A map contains [BlindMiniProfile] as a key and its corresponding location [GpsNode] as a value.
     */
    private fun drawMarkers(blindsNodes: Map<BlindMiniProfile, GpsNode?>) {
        blindsNodes.entries.forEach {
            val blindProfile = it.key
            val gpsNode = it.value
            if (!positions.containsKey(blindProfile.userName)) {
                Log.d("Marker", "New")
                // Draw a new marker if there was a marker before.
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
                        positions[blindProfile.userName] = marker
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
                    } else {
                        Log.d("Marker", "Marker from the map is null")
                    }
                } // TODO handle null cases
            }
        }
    }
}