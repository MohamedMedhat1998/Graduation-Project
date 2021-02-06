package com.mohamed.medhat.graduation_project.ui.confirmation_activity

import android.os.Bundle
import com.mohamed.medhat.graduation_project.dagger.scopes.ActivityScope
import com.mohamed.medhat.graduation_project.ui.base.AdvancedPresenter
import javax.inject.Inject

/**
 * An mvp presenter for [ConfirmationActivity].
 */
@ActivityScope
class ConfirmationPresenter @Inject constructor() :
    AdvancedPresenter<ConfirmationView, ConfirmationViewModel>() {

    private lateinit var confirmationView: ConfirmationView
    private lateinit var confirmationViewModel: ConfirmationViewModel
    private lateinit var activity: ConfirmationActivity

    override fun start(savedInstanceState: Bundle?) {
        activity = confirmationView as ConfirmationActivity
    }

    override fun setView(view: ConfirmationView) {
        confirmationView = view
    }

    override fun setViewModel(viewModel: ConfirmationViewModel) {
        confirmationViewModel = viewModel
    }

    fun onSubmitClicked() {
        confirmationView.displayToast(confirmationView.getConfirmationCode())
    }
}