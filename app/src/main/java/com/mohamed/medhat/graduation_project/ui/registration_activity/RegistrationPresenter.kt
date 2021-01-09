package com.mohamed.medhat.graduation_project.ui.registration_activity

import android.os.Bundle
import com.mohamed.medhat.graduation_project.dagger.scopes.ActivityScope
import com.mohamed.medhat.graduation_project.ui.base.AdvancedPresenter
import javax.inject.Inject

/**
 * An mvp presenter for the [RegistrationActivity].
 */
@ActivityScope
class RegistrationPresenter @Inject constructor() :
    AdvancedPresenter<RegistrationView, RegistrationViewModel>() {

    private lateinit var registrationView: RegistrationView
    private lateinit var registrationViewModel: RegistrationViewModel
    private lateinit var activity: RegistrationActivity

    override fun start(savedInstanceState: Bundle?) {
        activity = (registrationView as RegistrationActivity)
        activity.displayToast("RegistrationActivity")
    }

    override fun setView(view: RegistrationView) {
        registrationView = view
    }

    override fun setViewModel(viewModel: RegistrationViewModel) {
        registrationViewModel = viewModel
    }

    fun onAddPictureClicked() {
        activity.displayToast(
            "AddPicture\nFirstName: ${activity.getFirstName()}\n" +
                    "LastName: ${activity.getLastName()}\n" +
                    "PhoneNumber: ${activity.getPhoneNumber()}\n" +
                    "Email: ${activity.getEmail()}\n" +
                    "Password: ${activity.getPassword()}"
        )
    }

    fun onSubmitClicked() {
        activity.displayToast("Submit")
    }
}