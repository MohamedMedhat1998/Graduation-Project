package com.mohamed.medhat.sanad.ui.main_activity

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.GoogleMap
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.ui.base.BaseActivity
import javax.inject.Inject

/**
 * An activity for the main screen of the app.
 */
class MainActivity : BaseActivity(), MainView {

    @Inject
    lateinit var mainPresenter: MainPresenter

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
    }

    override fun onMapReady(p0: GoogleMap?) {

    }
}