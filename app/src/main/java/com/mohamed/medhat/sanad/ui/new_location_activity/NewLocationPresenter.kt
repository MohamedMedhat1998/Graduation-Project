package com.mohamed.medhat.sanad.ui.new_location_activity

import android.os.Bundle
import com.mohamed.medhat.sanad.ui.base.AdvancedPresenter
import javax.inject.Inject

/**
 * An mvp presenter for the [NewLocationActivity].
 */
class NewLocationPresenter @Inject constructor() :
    AdvancedPresenter<NewLocationView, NewLocationViewModel>() {

    private lateinit var newLocationView: NewLocationView
    private lateinit var newLocationViewModel: NewLocationViewModel
    private lateinit var activity: NewLocationActivity

    override fun start(savedInstanceState: Bundle?) {
        newLocationView.displayToast("NewLocation Hello World!")
    }

    override fun setView(view: NewLocationView) {
        newLocationView = view
        activity = view as NewLocationActivity
    }

    override fun setViewModel(viewModel: NewLocationViewModel) {
        newLocationViewModel = viewModel
    }
}