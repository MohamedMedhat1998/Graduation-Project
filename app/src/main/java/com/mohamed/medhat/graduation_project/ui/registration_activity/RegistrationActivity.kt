package com.mohamed.medhat.graduation_project.ui.registration_activity

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.mohamed.medhat.graduation_project.R
import com.mohamed.medhat.graduation_project.ui.base.BaseActivity
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
    }
}