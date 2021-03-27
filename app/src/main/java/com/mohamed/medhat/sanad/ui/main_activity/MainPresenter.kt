package com.mohamed.medhat.sanad.ui.main_activity

import android.os.Bundle
import com.google.android.gms.maps.SupportMapFragment
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.dagger.scopes.ActivityScope
import com.mohamed.medhat.sanad.ui.base.AdvancedPresenter
import javax.inject.Inject


/**
 * An mvp presenter for the main screen.
 */
@ActivityScope
class MainPresenter @Inject constructor() : AdvancedPresenter<MainView, MainViewModel>() {

    private lateinit var mainView: MainView
    private lateinit var mainViewModel: MainViewModel
    private lateinit var activity: MainActivity

    override fun start(savedInstanceState: Bundle?) {
        activity = mainView as MainActivity
        loadMap()
    }

    override fun setView(view: MainView) {
        mainView = view
    }

    override fun setViewModel(viewModel: MainViewModel) {
        mainViewModel = viewModel
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
}