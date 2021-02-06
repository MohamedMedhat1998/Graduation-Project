package com.mohamed.medhat.graduation_project.ui.confirmation_activity

import android.os.Bundle
import com.mohamed.medhat.graduation_project.R
import com.mohamed.medhat.graduation_project.dagger.scopes.ActivityScope
import com.mohamed.medhat.graduation_project.ui.base.AdvancedPresenter
import com.mohamed.medhat.graduation_project.ui.main_activity.MainActivity
import kotlinx.android.synthetic.main.activity_confirmation.*
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
        confirmationViewModel.isConfirmed.observe(activity) {
            // TODO handle successful confirmation
            if (it) {
                confirmationView.navigateToThenFinish(MainActivity::class.java)
            } else {
                confirmationView.displayToast("Something went wrong!")
            }
        }
        confirmationViewModel.state.observe(activity) {
            // TODO handle request state
        }
    }

    override fun setView(view: ConfirmationView) {
        confirmationView = view
    }

    override fun setViewModel(viewModel: ConfirmationViewModel) {
        confirmationViewModel = viewModel
    }

    fun onSubmitClicked() {
        if (confirmationView.getConfirmationCode().isEmpty()) {
            val emptyFieldError = activity.getString(R.string.empty_field_warning)
            confirmationView.showInputError(activity.et_confirmation_code, emptyFieldError)
        } else {
            confirmationViewModel.confirmEmail(confirmationView.getConfirmationCode())
        }
    }
}