package com.mohamed.medhat.sanad.ui.confirmation_activity

import android.os.Bundle
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.dagger.scopes.ActivityScope
import com.mohamed.medhat.sanad.ui.base.AdvancedPresenter
import com.mohamed.medhat.sanad.ui.base.error_viewers.TextErrorViewer
import com.mohamed.medhat.sanad.ui.login_activity.LoginActivity
import com.mohamed.medhat.sanad.ui.q_r_activity.QRActivity
import com.mohamed.medhat.sanad.utils.handleLoadingState
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
            if (it) {
                confirmationView.navigateToThenFinish(QRActivity::class.java)
            } else {
                confirmationView.displayToast("Something went wrong!")
            }
        }
        confirmationViewModel.state.observe(activity) {
            activity.setAppErrorViewer(
                TextErrorViewer(
                    confirmationViewModel.appError,
                    activity.tv_confirmation_error
                )
            )
            handleLoadingState(confirmationView, it)
        }
        confirmationViewModel.shouldReLogin.observe(activity) {
            if (it) {
                confirmationView.navigateToThenFinish(LoginActivity::class.java)
            }
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