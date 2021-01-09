package com.mohamed.medhat.graduation_project.ui.registration_activity

import android.os.Bundle
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

    override fun getFirstName(): String {
        return et_registration_first_name.text.toString()
    }

    override fun getLastName(): String {
        return et_registration_last_name.text.toString()
    }

    override fun getPhoneNumber(): String {
        return et_registration_phone_number.text.toString()
    }

    override fun getEmail(): String {
        return et_registration_email.text.toString()
    }

    override fun getPassword(): String {
        return et_registration_password.text.toString()
    }
}