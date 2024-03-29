package com.mohamed.medhat.sanad.ui.main_activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

/**
 * An activity for the main screen of the app.
 */
class MainActivity : BaseActivity(), MainView {

    @Inject
    lateinit var mainPresenter: MainPresenter
    private lateinit var map: GoogleMap
    private lateinit var marker: Marker

    private val mainViewModel: MainViewModel by lazy {
        ViewModelProviders.of(this, activityComponent.mainViewModel())
            .get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activityComponent.inject(this)
        mainPresenter.setView(this)
        mainPresenter.setViewModel(mainViewModel)
        mainPresenter.start(savedInstanceState)
        btn_main_options_menu.setOnClickListener {
            mainPresenter.onOptionsMenuButtonClicked()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mainPresenter.handleOnRequestPermissionResult(requestCode, permissions, grantResults)
    }

    override fun addPin(x: Double, y: Double) {
        addPin(x, y, "")
    }

    override fun addPin(x: Double, y: Double, title: String) {
        if (!::marker.isInitialized) {
            marker = map.addMarker(MarkerOptions().position(LatLng(x, y)).title(title))
        } else {
            marker.position = LatLng(x, y)
            marker.title = title
        }
    }

    override fun onMapReady(p0: GoogleMap?) {
        if (p0 != null) {
            map = p0
            mainPresenter.handleOnMapReady(map)
        }
    }

    override fun showLoadingIndicator() {
        pb_main_loading.visibility = View.VISIBLE
    }

    override fun hideLoadingIndicator() {
        pb_main_loading.visibility = View.INVISIBLE
    }

    override fun showError() {
        displayAppError()
    }

    override fun hideError() {
        hideAppError()
    }

    override fun onPause() {
        super.onPause()
        mainPresenter.activityPaused()
    }

    override fun onResume() {
        super.onResume()
        mainPresenter.activityResumed()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainPresenter.activityDestroyed()
    }
}