package com.mohamed.medhat.sanad.ui.persons_manager_activity

import android.os.Bundle
import com.mohamed.medhat.sanad.model.BlindMiniProfile
import com.mohamed.medhat.sanad.ui.base.AdvancedPresenter
import com.mohamed.medhat.sanad.utils.FRAGMENT_FEATURES_BLIND_PROFILE
import javax.inject.Inject

/**
 * An mvp presenter for the [PersonsManagerActivity].
 */
class PersonsManagerPresenter @Inject constructor() : AdvancedPresenter<PersonsManagerView, PersonsManagerViewModel>() {

    private lateinit var personsManagerView: PersonsManagerView
    private lateinit var personsManagerViewModel: PersonsManagerViewModel
    private lateinit var activity: PersonsManagerActivity
    private lateinit var blindMiniProfile: BlindMiniProfile

    override fun start(savedInstanceState: Bundle?) {
        personsManagerView.displayToast("Persons Manager: ${blindMiniProfile.firstName}")
    }

    override fun setView(view: PersonsManagerView) {
        personsManagerView = view
        activity = view as PersonsManagerActivity
        blindMiniProfile =
            activity.intent.extras?.getSerializable(FRAGMENT_FEATURES_BLIND_PROFILE) as BlindMiniProfile
    }

    override fun setViewModel(viewModel: PersonsManagerViewModel) {
        personsManagerViewModel = viewModel
    }
}