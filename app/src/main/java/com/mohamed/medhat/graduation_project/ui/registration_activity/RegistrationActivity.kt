package com.mohamed.medhat.graduation_project.ui.registration_activity

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.lifecycle.ViewModelProviders
import com.mohamed.medhat.graduation_project.R
import com.mohamed.medhat.graduation_project.ui.base.BaseActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        activityComponent.inject(this)
        registrationPresenter.setView(this)
        registrationPresenter.setViewModel(registrationViewModel)
        registrationPresenter.start(savedInstanceState)
        btn_registration_add_picture.setOnClickListener {
            registrationPresenter.onAddPictureClicked()
        }
        btn_registration_submit.setOnClickListener {
            registrationPresenter.onSubmitClicked()
        }
    }

    override fun getFullName(): String {
        return et_registration_full_name.text.toString()
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

    override fun showErrorMessage() {
        tv_registration_error.visibility = View.VISIBLE
    }

    override fun hideErrorMessage() {
        tv_registration_error.visibility = View.INVISIBLE
    }

    override fun setErrorMessage(message: String) {
        tv_registration_error.text = message
    }
}