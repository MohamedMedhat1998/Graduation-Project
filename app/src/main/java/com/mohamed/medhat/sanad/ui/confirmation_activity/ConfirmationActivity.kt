package com.mohamed.medhat.sanad.ui.confirmation_activity

import android.os.Bundle
import android.view.View
import android.widget.EditText
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
        tv_confirmation_resend.setOnClickListener {
            confirmationPresenter.onResendClicked()
        }
    }

    override fun getConfirmationCode(): String {
        val n1 = c_1.text.toString()
        val n2 = c_2.text.toString()
        val n3 = c_3.text.toString()
        val n4 = c_4.text.toString()
        val n5 = c_5.text.toString()
        val n6 = c_6.text.toString()
        return "$n1$n2$n3$n4$n5$n6"
    }

    override fun getConfirmationViews(): List<EditText> {
        val views = mutableListOf<EditText>()
        views.apply {
            add(c_1)
            add(c_2)
            add(c_3)
            add(c_4)
            add(c_5)
            add(c_6)
        }
        return views
    }

    override fun updateWaitTime(secondsLeft: Int) {
        tv_confirmation_wait_time.text = secondsLeft.toString()
    }

    override fun hideWaitTime() {
        tv_confirmation_wait_time.visibility = View.INVISIBLE
    }

    override fun showWaitTime() {
        tv_confirmation_wait_time.visibility = View.VISIBLE
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