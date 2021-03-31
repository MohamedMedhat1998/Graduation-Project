package com.mohamed.medhat.sanad.ui.confirmation_activity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_confirmation.*
import javax.inject.Inject

/**
 * An activity for the confirmation screen.
 */
class ConfirmationActivity : BaseActivity(), ConfirmationView {

    @Inject
    lateinit var confirmationPresenter: ConfirmationPresenter

    private val confirmationViewModel: ConfirmationViewModel by lazy {
        ViewModelProviders.of(this, activityComponent.confirmationViewModel())
            .get(ConfirmationViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)
        activityComponent.inject(this)
        confirmationPresenter.setView(this)
        confirmationPresenter.setViewModel(confirmationViewModel)
        confirmationPresenter.start(savedInstanceState)
        btn_confirmation_submit.setOnClickListener {
            confirmationPresenter.onSubmitClicked()
        }
    }

    override fun getConfirmationCode(): String {
        return et_confirmation_code.text.toString()
    }

    override fun showLoadingIndicator() {
        pb_confirmation_loading_indicator.visibility = View.VISIBLE
    }

    override fun hideLoadingIndicator() {
        pb_confirmation_loading_indicator.visibility = View.INVISIBLE
    }

    override fun showError() {
        tv_confirmation_error.visibility = View.VISIBLE
        displayAppError()
    }

    override fun hideError() {
        tv_confirmation_error.visibility = View.INVISIBLE
        hideAppError()
    }
}