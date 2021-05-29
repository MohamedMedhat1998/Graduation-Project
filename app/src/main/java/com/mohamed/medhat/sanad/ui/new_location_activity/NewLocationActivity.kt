package com.mohamed.medhat.sanad.ui.new_location_activity

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.ui.base.BaseActivity
import javax.inject.Inject

/**
 * An activity that enables the user to enter the data of the new favorite location to add.
 */
class NewLocationActivity : BaseActivity(), NewLocationView {

    @Inject
    lateinit var newLocationPresenter: NewLocationPresenter

    private val newLocationViewModel: NewLocationViewModel by lazy {
        ViewModelProviders.of(this, activityComponent.newLocationViewModel())
            .get(NewLocationViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_location)
        activityComponent.inject(this)
        newLocationPresenter.setView(this)
        newLocationPresenter.setViewModel(newLocationViewModel)
        newLocationPresenter.start(savedInstanceState)
    }
}