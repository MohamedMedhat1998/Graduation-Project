package com.mohamed.medhat.sanad.ui.confirmation_activity

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.dagger.scopes.ActivityScope
import com.mohamed.medhat.sanad.ui.base.AdvancedPresenter
import com.mohamed.medhat.sanad.ui.base.error_viewers.TextErrorViewer
import com.mohamed.medhat.sanad.ui.login_activity.LoginActivity
import com.mohamed.medhat.sanad.ui.q_r_activity.QRActivity
import com.mohamed.medhat.sanad.utils.handleLoadingState
import kotlinx.android.synthetic.main.activity_confirmation.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val CONFIRMATION_CODE_SIZE = 6
private const val RESEND_WAIT_TIME = 60

/**
 * An mvp presenter for [ConfirmationActivity].
 */
@ActivityScope
class ConfirmationPresenter @Inject constructor() :
    AdvancedPresenter<ConfirmationView, ConfirmationViewModel>() {

    private lateinit var confirmationView: ConfirmationView
    private lateinit var confirmationViewModel: ConfirmationViewModel
    private lateinit var activity: ConfirmationActivity
    private var canResendConfirmationCode = true
    private var waitTimeLeft = MutableLiveData<Int>()

    override fun start(savedInstanceState: Bundle?) {
        ConfirmationHandler.handle(confirmationView.getConfirmationViews())
        confirmationViewModel.isConfirmed.observe(activity) {
            if (it) {
                confirmationView.navigateToThenFinish(QRActivity::class.java)
            } else {
                confirmationView.displayToast("Something went wrong!")
            }
        }
        confirmationViewModel.state.observe(activity) {
            confirmationView.setAppErrorViewer(
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
        confirmationViewModel.resendSuccess.observe(activity) {
            if (it) {
                confirmationView.displayToast(activity.getString(R.string.resend_success_message))
            }
        }
        waitTimeLeft.observe(activity) {
            if (it <= 0) {
                confirmationView.hideWaitTime()
            } else {
                confirmationView.showWaitTime()
                confirmationView.updateWaitTime(it)
            }
        }
    }

    override fun setView(view: ConfirmationView) {
        confirmationView = view
        activity = confirmationView as ConfirmationActivity
    }

    override fun setViewModel(viewModel: ConfirmationViewModel) {
        confirmationViewModel = viewModel
    }

    fun onSubmitClicked() {
        if (confirmationView.getConfirmationCode().length < CONFIRMATION_CODE_SIZE) {
            confirmationView.displayToast(activity.getString(R.string.confirmation_code_warning))
        } else {
            confirmationViewModel.confirmEmail(confirmationView.getConfirmationCode())
        }
    }

    fun onResendClicked() {
        if (canResendConfirmationCode) {
            confirmationViewModel.resendConfirmationCode()
            canResendConfirmationCode = false
            waitTimeLeft.value = RESEND_WAIT_TIME
            CoroutineScope(Dispatchers.IO).launch {
                while (waitTimeLeft.value != 0) {
                    delay(1000)
                    waitTimeLeft.postValue(waitTimeLeft.value!! - 1)
                }
                canResendConfirmationCode = true
            }
        } else {
            confirmationView.displayToast(
                activity.getString(
                    R.string.resend_confirmation_wait_message,
                    waitTimeLeft.value
                )
            )
        }
    }
}