package com.mohamed.medhat.sanad.ui.registration_activity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.mohamed.medhat.sanad.R
import com.mohamed.medhat.sanad.ui.CommonNavViewModel
import com.mohamed.medhat.sanad.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_registration.*
import javax.inject.Inject

/**
 * An activity for the registration screen.
 */
class RegistrationActivity : BaseActivity(), RegistrationView {

    @Inject
    lateinit var registrationPresenter: RegistrationPresenter

    private val registrationViewModel by lazy {
        ViewModelProviders.of(this, activityComponent.registrationViewModel())
            .get(RegistrationViewModel::class.java)
    }

    private val commonNavViewModel by lazy {
        ViewModelProviders.of(this, activityComponent.commonNavViewModel())
            .get(CommonNavViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        activityComponent.inject(this)
        registrationPresenter.setView(this)
        registrationPresenter.setViewModel(registrationViewModel)
        registrationPresenter.setNavigationViewModel(commonNavViewModel)
        registrationPresenter.start(savedInstanceState)
        tv_registration_login.setOnClickListener {
            registrationPresenter.onLoginClicked()
        }
        btn_registration_submit.setOnClickListener {
            registrationPresenter.onSubmitClicked()
        }
    }

    override fun getFirstName(): String {
        return et_registration_first_name.text.toString()
    }

    override fun getLastName(): String {
        return et_registration_last_name.text.toString()
    }

    override fun getEmail(): String {
        return et_registration_email.text.toString()
    }

    override fun getPassword(): String {
        return et_registration_password.text.toString()
    }

    override fun getConfirmedPassword(): String {
        return et_registration_confirm_password.text.toString()
    }

    override fun showLoadingIndicator() {
        pb_registration_loading.visibility = View.VISIBLE
    }

    override fun hideLoadingIndicator() {
        pb_registration_loading.visibility = View.INVISIBLE
    }

    override fun showError() {
        displayAppError()
    }

    override fun hideError() {
        hideAppError()
    }
}